package com.self.flipcart.serviceimpl;

import com.self.flipcart.cache.CacheStore;
import com.self.flipcart.dto.Attempt;
import com.self.flipcart.dto.MessageData;
import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.exceptions.*;
import com.self.flipcart.model.*;
import com.self.flipcart.repository.AccessTokenRepo;
import com.self.flipcart.repository.RefreshTokenRepo;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.repository.UserRepo;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.security.JwtService;
import com.self.flipcart.service.AuthService;
import com.self.flipcart.util.CookieManager;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private SellerRepo sellerRepo;
    private UserRepo userRepo;
    private AccessTokenRepo accessTokenRepo;
    private RefreshTokenRepo refreshTokenRepo;
    private ResponseStructure<UserResponse> structure;
    private ResponseStructure<AuthResponse> authStructure;
    private SimpleResponseStructure simpleResponseStructure;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender javaMailSender;
    private CacheStore<OtpModel> otpCache;
    private CacheStore<User> userCacheStore;
    private CacheStore<Attempt> attemptCacheStore;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private CookieManager cookieManager;

    public AuthServiceImpl(SellerRepo sellerRepo,
                           UserRepo userRepo,
                           AccessTokenRepo accessTokenRepo,
                           RefreshTokenRepo refreshTokenRepo,
                           ResponseStructure<UserResponse> structure,
                           ResponseStructure<AuthResponse> authStructure,
                           SimpleResponseStructure simpleResponseStructure,
                           PasswordEncoder passwordEncoder,
                           JavaMailSender javaMailSender,
                           CacheStore<OtpModel> otpCache,
                           CacheStore<User> userCacheStore,
                           CacheStore<Attempt> attemptCacheStore,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           CookieManager cookieManager) {
        this.sellerRepo = sellerRepo;
        this.userRepo = userRepo;
        this.accessTokenRepo = accessTokenRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.structure = structure;
        this.authStructure = authStructure;
        this.simpleResponseStructure = simpleResponseStructure;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.otpCache = otpCache;
        this.userCacheStore = userCacheStore;
        this.attemptCacheStore = attemptCacheStore;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.cookieManager = cookieManager;
    }

    @Value("${token.expiry.access.seconds}")
    private long accessTokenExpirySeconds;
    @Value("${token.expiry.refresh.seconds}")
    private long refreshTokenExpirySeconds;

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {
        // validating if there is already a user with the given email in the request
        if (userRepo.existsByEmail(userRequest.getEmail()))
            throw new UserAlreadyExistsByEmailException("Failed To register the User");
        User user = mapToChildEntity(userRequest);
        // caching user data
        userCacheStore.add(user.getEmail(), user);
        // Generate the OTP and provide the ID of the OTP as a path variable to the confirmation link.
        OtpModel otp = OtpModel.builder()
                .otp(generateOTP())
                .email(user.getEmail()).build();
        otpCache.add(otp.getEmail(), otp);
        try {
            sendOTPToMailId(user, otp.getOtp());
            return new ResponseEntity<>(
                    structure.setStatus(HttpStatus.ACCEPTED.value())
                            .setMessage("user registration successful. Please check your email for OTP")
                            .setData(mapToUserResponse(user)), HttpStatus.ACCEPTED);
        } catch (MessagingException e) {
            throw new EmailNotFoundException("Failed to verify the email ID");
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(OtpModel otpModel) {
        OtpModel otp = otpCache.get(otpModel.getEmail());
        User user = userCacheStore.get(otpModel.getEmail());
        if (otp == null) throw new OtpExpiredException("Failed to verify OTP");
        if (user == null) throw new RegistrationSessionExpiredException("Failed to verify OTP");
        if (otp.getOtp() != otpModel.getOtp()) throw new IncorrectOTPException("Failed to verify OTP");
        user.setEmailVerified(true);
        userRepo.save(user);
        otpCache.remove(otpModel.getEmail());
        try {
        sendConfirmationMail(user);
        return new ResponseEntity<>(structure.setStatus(HttpStatus.OK.value())
                .setMessage("User registration successful")
                .setData(mapToUserResponse(user)), HttpStatus.OK);
        } catch (MessagingException e) {
            throw new EmailNotFoundException("Failed to send confirmation mail");
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest, String refreshToken, String accessToken) {
        // validate if the user is already logged in
        if (accessToken != null && refreshToken != null) throw new UserAlreadyLoggedInException("Failed to login");
        // getting username
        String username = authRequest.getEmail().split("@")[0];
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

        // validating if the user authentication is authenticated
        if (auth.isAuthenticated()) return userRepo.findByUsername(username).map(this::generateAuthResponse).get();
        else throw new UsernameNotFoundException("Authentication failed");
    }

    private ResponseEntity<ResponseStructure<AuthResponse>> generateAuthResponse(User user){
        // granting access to User with new access and refresh token cookies in response
        HttpHeaders headers = grantAccessToUser(user);
        return ResponseEntity.ok().headers(headers).body(authStructure.setStatus(HttpStatus.OK.value())
                .setMessage("Login refreshed successfully")
                .setData(AuthResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .role(user.getUserRole().name())
                        .isAuthenticated(true)
                        .accessExpiration(accessTokenExpirySeconds)
                        .refreshExpiration(refreshTokenExpirySeconds)
                        .build()));
    }

    @Override
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken, String accessToken) {
        if (refreshToken == null) throw new UserNotLoggedInException("Failed to refresh login");
        Attempt attempt = attemptCacheStore.get(refreshToken);
        if (attempt != null)
            if (attempt.getLastAttemptedAt().isAfter(LocalDateTime.now()))
                throw new TooManyAttemptsException("Failed to refresh login");
            else attemptCacheStore.add(refreshToken, new Attempt(LocalDateTime.now().plusSeconds(10)));
        if (accessToken != null) blockAccessToken(accessToken);

        String username = refreshTokenRepo.findByToken(refreshToken).map(rt -> {
            if (rt.isBlocked()) throw new UserNotLoggedInException("Failed to refresh login");
            else return jwtService.extractUsername(refreshToken);
        }).orElseThrow(() -> new UserNotLoggedInException("Failed to refresh login"));
        return userRepo.findByUsername(username).map(user -> {
            // blocking old token
            blockRefreshToken(refreshToken);
            return generateAuthResponse(user);

        }).orElseThrow(() -> new UsernameNotFoundException("Failed to refresh login"));
    }

    @Override
    public ResponseEntity<SimpleResponseStructure> logout(String refreshToken, String accessToken) {

        // resetting tokens with blank value and 0 maxAge
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.invalidate("at"));
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.invalidate("rt"));
        // blocking the tokens
        blockAccessToken(accessToken);
        blockRefreshToken(refreshToken);

        return ResponseEntity.ok().headers(headers).body(simpleResponseStructure.setStatus(HttpStatus.OK.value())
                .setMessage("Logout Successful"));
    }

    @Override
    public ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(String refreshToken, String accessToken) {
        if (refreshToken == null || accessToken == null)
            throw new UserNotLoggedInException("Failed to revoke access from all other devices");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByUsername(username).map(user -> {
            // blocking all other access tokens
            List<AccessToken> accessTokens = accessTokenRepo.findAllByUserAndIsBlocked(user, false).stream()
                    .peek(at -> {
                        if (!at.getToken().equals(accessToken)) at.setBlocked(true);
                    })
                    .collect(Collectors.toList());
            accessTokenRepo.saveAll(accessTokens);
            // blocking all other refresh tokens
            List<RefreshToken> refreshTokens = refreshTokenRepo.findALLByUserAndIsBlocked(user, false).stream()
                    .peek(rt -> {
                        if (!rt.getToken().equals(refreshToken)) rt.setBlocked(true);
                    }).collect(Collectors.toList());
            refreshTokenRepo.saveAll(refreshTokens);

            return ResponseEntity.ok(simpleResponseStructure.setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully revoked access from all other devices"));

        }).orElseThrow(() -> new UsernameNotFoundException("Failed to revoke access fromm all other devices"));
    }

    @Override
    public ResponseEntity<SimpleResponseStructure> revokeAllTokens() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByUsername(username).map(user -> {
            // blocking all other access tokens
            List<AccessToken> accessTokens = accessTokenRepo.findAllByUserAndIsBlocked(user, false).stream()
                    .peek(at -> at.setBlocked(true)).collect(Collectors.toList());
            accessTokenRepo.saveAll(accessTokens);
            // blocking all other refresh tokens
            List<RefreshToken> refreshTokens = refreshTokenRepo.findALLByUserAndIsBlocked(user, false).stream()
                    .peek(rt -> rt.setBlocked(true)).collect(Collectors.toList());
            refreshTokenRepo.saveAll(refreshTokens);

            // resetting tokens with blank value and 0 maxAge
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookieManager.invalidate("at"));
            headers.add(HttpHeaders.SET_COOKIE, cookieManager.invalidate("rt"));

            return ResponseEntity.ok().headers(headers).body(simpleResponseStructure.setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully revoked access from all devices"));

        }).orElseThrow(() -> new UsernameNotFoundException("Failed to revoke access fromm all other devices"));
    }

    /* ----------------------------------------------------------------------------------------------------------- */
    private HttpHeaders grantAccessToUser(User user) {
        //generating access and refresh tokens
        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getUserRole().name());
        String newAccessToken = jwtService.generateAccessToken(user.getUsername(), user.getUserRole().name());

        attemptCacheStore.add(newRefreshToken, new Attempt(LocalDateTime.now()));

        // adding cookies to the HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("at", newAccessToken, accessTokenExpirySeconds));
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("rt", newRefreshToken, refreshTokenExpirySeconds));

        // saving access and refresh tokens to the database
        accessTokenRepo.save(AccessToken.builder()
                .isBlocked(false)
                .token(newAccessToken)
                .expiration(LocalDateTime.now().plusSeconds(accessTokenExpirySeconds))
                .user(user).build());
        refreshTokenRepo.save(RefreshToken.builder()
                .isBlocked(false)
                .token(newRefreshToken)
                .expiration(LocalDateTime.now().plusSeconds(refreshTokenExpirySeconds))
                .user(user).build());

        return headers;
    }

    private void blockAccessToken(String accessToken) {
        accessTokenRepo.findByToken(accessToken).ifPresent(at -> {
            at.setBlocked(true);
            accessTokenRepo.save(at);
        });
    }

    private void blockRefreshToken(String refreshToken) {
        refreshTokenRepo.findByToken(refreshToken).ifPresent(rt -> {
            rt.setBlocked(true);
            refreshTokenRepo.save(rt);
        });
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .email(user.getEmail())
                .isEmailVerified(user.isEmailVerified())
                .build();
    }

    private <T extends User> T mapToChildEntity(UserRequest userRequest) {
        User user = null;
        switch (userRequest.getUserRole()) {
            case SELLER -> user = new Seller();
            case CUSTOMER -> user = new Customer();
            default -> throw new InvalidUserRoleException("Failed to process the request");
        }
        if(user != null){
            user.setUsername(userRequest.getEmail().split("@")[0]);
            user.setEmail(userRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setUserRole(userRequest.getUserRole());
        }
        return (T) user;
    }

    private Integer generateOTP() {
        return new Random().nextInt(100000, 999999);
    }

    private void sendOTPToMailId(User user, int otp) throws MessagingException {
        sendMail(MessageData.builder()
                .to(user.getEmail())
                .subject("Verify your email for flipkart")
                .sentDate(new Date())
                .text(
                        "Hi " + user.getEmail().split("@")[0] + ",<br>"
                                + "<h4> Nice to see you interested in Flipkart, your OTP for email verification is,</h4><br><br>"
                                + "<h3 style=\"color: #1D63FF; font-size: 1rem; font-weight: 600; text-decoration: none; padding: 0.5em 1em;"
                                + "border-radius: 10px; width: max-content;\">" + otp + "</h3>" // add the OTP ID (UUID)
                                + "<br><br>"
                                + "With Best Regards,<br>"
                                + "Flipkart"
                ).build());
    }

    @Async
    private void sendMail(MessageData messageData) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(messageData.getTo());
        helper.setSubject(messageData.getSubject());
        helper.setSentDate(messageData.getSentDate());
        helper.setText(messageData.getText(), true);
        javaMailSender.send(message);
    }

    @Async
    private void sendConfirmationMail(User user) throws MessagingException {
        sendMail(MessageData.builder()
                .to(user.getEmail())
                .subject("Welcome to Flipkart family")
                .sentDate(new Date())
                .text(
                        "<b>Congratulations, your now a part of flipkart family, your email verification is " +
                                "successfully completed</b>" +
                                "<br><br>" +
                                "With Best Regards,<br>" +
                                "Flipkart"
                )
                .build());
    }
}
