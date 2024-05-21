package com.devb.estores.serviceimpl;

import com.devb.estores.cache.CacheStore;
import com.devb.estores.dto.MessageData;
import com.devb.estores.dto.OtpModel;
import com.devb.estores.enums.UserRole;
import com.devb.estores.exceptions.*;
import com.devb.estores.model.AccessToken;
import com.devb.estores.model.RefreshToken;
import com.devb.estores.model.User;
import com.devb.estores.repository.AccessTokenRepo;
import com.devb.estores.repository.RefreshTokenRepo;
import com.devb.estores.repository.UserRepo;
import com.devb.estores.requestdto.AuthRequest;
import com.devb.estores.requestdto.UserRequest;
import com.devb.estores.responsedto.AuthResponse;
import com.devb.estores.security.JwtService;
import com.devb.estores.service.AuthService;
import com.devb.estores.util.CookieManager;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import com.self.flipkart.exceptions.*;
import com.devb.estores.responsedto.UserResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final AccessTokenRepo accessTokenRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final CacheStore<OtpModel> otpCache;
    private final CacheStore<User> userCacheStore;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieManager cookieManager;

    public AuthServiceImpl(UserRepo userRepo,
                           AccessTokenRepo accessTokenRepo,
                           RefreshTokenRepo refreshTokenRepo,
                           PasswordEncoder passwordEncoder,
                           JavaMailSender javaMailSender,
                           CacheStore<OtpModel> otpCache,
                           CacheStore<User> userCacheStore,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           CookieManager cookieManager) {
        this.userRepo = userRepo;
        this.accessTokenRepo = accessTokenRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.otpCache = otpCache;
        this.userCacheStore = userCacheStore;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.cookieManager = cookieManager;
    }

    @Value("${token.expiry.access.seconds}")
    private long accessTokenExpirySeconds;
    @Value("${token.expiry.refresh.seconds}")
    private long refreshTokenExpirySeconds;

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest, UserRole role) {
        // validating if there is already a user with the given email in the request
        if (userRepo.existsByEmail(userRequest.getEmail()))
            throw new UserAlreadyExistsByEmailException("Failed To register the User");

        User user = mapToUserEntity(userRequest, role);

        // caching user data
        userCacheStore.add(user.getEmail(), user);

        // Generate the OTP and provide the ID of the OTP as a path variable to the confirmation link.
        OtpModel otp = OtpModel.builder()
                .otp(generateOTP())
                .email(user.getEmail()).build();
        otpCache.add(otp.getEmail(), otp);

        try {
            sendOTPToMailId(user, otp.getOtp());
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ResponseStructure<UserResponse>()
                            .setStatus(HttpStatus.ACCEPTED.value())
                            .setMessage("user registration successful. Please check your email for OTP")
                            .setData(mapToUserResponse(user)));
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
        user = userRepo.save(user);

        otpCache.remove(otpModel.getEmail());
        try {
            sendConfirmationMail(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseStructure<UserResponse>()
                            .setStatus(HttpStatus.OK.value())
                            .setMessage("User registration successful")
                            .setData(mapToUserResponse(user)));
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
        if (auth.isAuthenticated()) return userRepo.findByUsername(username).map(user -> {
            HttpHeaders headers = new HttpHeaders();
            generateAccessToken(user, headers);
            generateRefreshToken(user, headers);
            return ResponseEntity.ok().headers(headers).body(new ResponseStructure<AuthResponse>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Login refreshed successfully")
                    .setData(AuthResponse.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .roles(user.getRoles().stream().map(UserRole::name).collect(Collectors.toList()))
                            .isAuthenticated(true)
                            .accessExpiration(accessTokenExpirySeconds)
                            .refreshExpiration(refreshTokenExpirySeconds)
                            .build()));
        }).orElseThrow(() -> new UsernameNotFoundException("Failed to refresh login"));
        else throw new UsernameNotFoundException("Authentication failed");
    }

    @Override
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken, String accessToken) {
        if (refreshToken == null) throw new UserNotLoggedInException("Failed to refresh login");

        String username = jwtService.extractUsername(refreshToken);
        Date refreshExpiration = jwtService.extractExpiry(refreshToken);
        Date refreshIssuedAt = jwtService.extractIssuedAt(refreshToken);
        Date accessExpiration = null;

        /* set accessToken to null if the token is invalid,
        ensuring the that there are no unnecessary exception thrown to the user
         */
        try {
            accessExpiration = accessToken != null ? jwtService.extractExpiry(accessToken) : null;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            accessToken = null;
        }

        // getting the leftover expiration duration for refresh token and access token
        long evaluatedAccessExpiration = accessExpiration != null
                ? accessTokenExpirySeconds - ((new Date().getTime() - accessExpiration.getTime()) / 1000)
                : accessTokenExpirySeconds;
        boolean newRefreshRequired = refreshIssuedAt.before(new Date()) && (refreshIssuedAt.getTime() - new Date().getTime()) > 24 * 60 * 60 * 1000;
        long evaluatedRefreshExpiration = newRefreshRequired
                ? refreshTokenExpirySeconds
                : refreshTokenExpirySeconds - ((new Date().getTime() - refreshExpiration.getTime()) / 1000);

        HttpHeaders headers = new HttpHeaders();

        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Failed to refresh login"));
        // validating if the accessToken is not already present
        if (accessToken == null) this.generateAccessToken(user, headers);

        // validating if the refresh token was issued before 24 hours.
        if (newRefreshRequired) {
            // blocking old token
            this.blockRefreshToken(refreshToken);
            // generating new refresh token
            this.generateRefreshToken(user, headers);
        }

        return ResponseEntity.ok().headers(headers).body(new ResponseStructure<AuthResponse>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Login refreshed successfully")
                .setData(AuthResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .roles(user.getRoles().stream().map(UserRole::name).collect(Collectors.toList()))
                        .isAuthenticated(true)
                        .accessExpiration(evaluatedAccessExpiration)
                        .refreshExpiration(evaluatedRefreshExpiration)
                        .build()));
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

        return ResponseEntity.ok().headers(headers).body(new SimpleResponseStructure().setStatus(HttpStatus.OK.value())
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

            return ResponseEntity.ok(new SimpleResponseStructure().setStatus(HttpStatus.OK.value())
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

            return ResponseEntity.ok().headers(headers).body(new SimpleResponseStructure().setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully revoked access from all devices"));

        }).orElseThrow(() -> new UsernameNotFoundException("Failed to revoke access fromm all other devices"));
    }

    /* ----------------------------------------------------------------------------------------------------------- */
    private void generateAccessToken(User user, HttpHeaders headers) {
        //generating access token
        String newAccessToken = jwtService.generateAccessToken(user.getUsername(), user.getRoles().stream().map(UserRole::name).toList().toString());

        // adding cookies to the HttpHeaders
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("at", newAccessToken, accessTokenExpirySeconds));

        // saving access token to the database
        accessTokenRepo.save(AccessToken.builder()
                .isBlocked(false)
                .token(newAccessToken)
                .expiration(LocalDateTime.now().plusSeconds(accessTokenExpirySeconds))
                .user(user).build());
    }

    private void generateRefreshToken(User user, HttpHeaders headers) {
        //generating access token
        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getRoles().stream().map(UserRole::name).toList().toString());

        // adding cookies to the HttpHeaders
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("rt", newRefreshToken, refreshTokenExpirySeconds));

        // saving refresh token to the database
        refreshTokenRepo.save(RefreshToken.builder()
                .isBlocked(false)
                .token(newRefreshToken)
                .expiration(LocalDateTime.now().plusSeconds(refreshTokenExpirySeconds))
                .user(user).build());
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
                .roles(user.getRoles().stream().map(UserRole::name).collect(Collectors.toList()))
                .email(user.getEmail())
                .isEmailVerified(user.isEmailVerified())
                .build();
    }

    private User mapToUserEntity(UserRequest userRequest, UserRole role) {
        return User.builder()
                .username(userRequest.getEmail().split("@gmail.com")[0])
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(role.equals(UserRole.SELLER)
                        ? Arrays.asList(UserRole.SELLER, UserRole.CUSTOMER)
                        : List.of(UserRole.CUSTOMER))
                .build();
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
