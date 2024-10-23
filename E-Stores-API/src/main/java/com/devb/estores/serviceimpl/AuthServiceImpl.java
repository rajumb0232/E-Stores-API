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
import com.devb.estores.responsedto.UserResponse;
import com.devb.estores.security.JwtService;
import com.devb.estores.service.AuthService;
import com.devb.estores.util.CookieManager;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final AccessTokenRepo accessTokenRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final CacheStore<OtpModel> otpCache;
    private final CacheStore<User> userCacheStore;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieManager cookieManager;
    private final Random random;

    public AuthServiceImpl(UserRepo userRepo,
                           AccessTokenRepo accessTokenRepo,
                           RefreshTokenRepo refreshTokenRepo,
                           PasswordEncoder passwordEncoder,
                           MailService mailService, CacheStore<OtpModel> otpCache,
                           CacheStore<User> userCacheStore,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           CookieManager cookieManager,
                           Random random) {
        this.userRepo = userRepo;
        this.accessTokenRepo = accessTokenRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.otpCache = otpCache;
        this.userCacheStore = userCacheStore;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.cookieManager = cookieManager;
        this.random = random;
    }

    @Value("${token.expiry.access.seconds}")
    private long accessTokenExpirySeconds;
    @Value("${token.expiry.refresh.seconds}")
    private long refreshTokenExpirySeconds;

    public static final String FAILED_REFRESH = "Failed to refresh login";
    public static final String FAILED_OTP_VERIFICATION = "Failed to verify OTP";

    @Override
    public String registerUser(UserRequest userRequest, UserRole role) {
        // validating if there is already a user with the given email in the request
        if (userRepo.existsByEmail(userRequest.getEmail()))
            throw new UserAlreadyExistsByEmailException("Failed To register the User");

        User user = mapToUserEntity(userRequest, role);

        // caching user data
        userCacheStore.add(user.getEmail(), user);

        // Generate the OTP and provide the ID of the OTP as a path variable to the confirmation link.
        OtpModel otp = OtpModel.builder()
                .otp(random.nextInt(100000, 999999))
                .email(user.getEmail()).build();
        otpCache.add(otp.getEmail(), otp);

        try {
            sendOTPToMailId(user, otp.getOtp());
            return "Verify Email using the OTP sent to " + user.getEmail();
        } catch (MessagingException e) {
            throw new EmailNotFoundException("Failed to verify the email ID");
        }
    }

    @Override
    public UserResponse verifyUserEmail(OtpModel otpModel) {
        OtpModel otp = otpCache.get(otpModel.getEmail());
        User user = userCacheStore.get(otpModel.getEmail());

        if (otp == null) throw new OtpExpiredException(FAILED_OTP_VERIFICATION);
        if (user == null) throw new RegistrationSessionExpiredException(FAILED_OTP_VERIFICATION);
        if (otp.getOtp() != otpModel.getOtp()) throw new IncorrectOTPException(FAILED_OTP_VERIFICATION);

        user.setEmailVerified(true);
        user = userRepo.save(user);

        otpCache.remove(otpModel.getEmail());
        try {
            sendConfirmationMail(user);
            return mapToUserResponse(user);
        } catch (MessagingException e) {
            throw new EmailNotFoundException("Failed to send confirmation mail");
        }
    }

    @Override
    public AuthResponse login(AuthRequest authRequest, String refreshToken, String accessToken) {
        // Authenticating user
        User user = this.authenticateUser(authRequest.getEmail(), authRequest.getPassword());

        return this.generateAuthResponse(user, accessTokenExpirySeconds, refreshTokenExpirySeconds);
    }

    private User authenticateUser(String email, String password) {
        String username = email.split("@")[0];
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // validating if the user authentication is authenticated
        if (!auth.isAuthenticated())
            throw new BadCredentialsException("The given credentials are incorrect");

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FAILED_REFRESH));
    }

    private AuthResponse generateAuthResponse(User user, long accessExpiryInSeconds, long refreshExpirySeconds) {
        return AuthResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(UserRole::name).toList())
                .isAuthenticated(true)
                .accessExpiration(accessExpiryInSeconds)
                .refreshExpiration(refreshExpirySeconds)
                .build();
    }

    @Override
    public HttpHeaders grantLoginAccess(AuthResponse authResponse) {
        HttpHeaders headers = new HttpHeaders();

        if (authResponse.getAccessExpiration() == accessTokenExpirySeconds)
            generateAccessToken(authResponse.getUsername(), authResponse.getRoles(), headers);

        if (authResponse.getRefreshExpiration() == refreshTokenExpirySeconds)
            generateRefreshToken(authResponse.getUsername(), authResponse.getRoles(), headers);

        return headers;
    }

    @Override
    public AuthResponse refreshLogin(String refreshToken, String accessToken) {
        if (refreshToken == null) throw new UserNotLoggedInException(FAILED_REFRESH);

        String username = jwtService.extractUsername(refreshToken);
        Date refreshExpiration = jwtService.extractExpiry(refreshToken);
        Date refreshIssuedAt = jwtService.extractIssuedAt(refreshToken);
        Date accessExpiration = accessToken != null ? this.getAccessExpiration(accessToken) : null;

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FAILED_REFRESH));

        /*Calculating new Access Expiration -
        Updates new time if the access token is valid, else sets default access time
          */
        long evaluatedAccessExpiration = accessTokenExpirySeconds;
        if (accessExpiration != null) {
            evaluatedAccessExpiration = this.getLeftOverSeconds(accessExpiration);
        }

        /* Validating if the refresh token was issued before 24 hours.
        If yes, the token is blocked and the evaluatedRefreshExpiration will be the default refresh expiration time
        else, the evaluatedRefreshExpiration will be the leftover time for expiration
         */
        long evaluatedRefreshExpiration = refreshTokenExpirySeconds;
        if (!this.isNewRefreshRequired(refreshIssuedAt)) {
            evaluatedRefreshExpiration = this.getLeftOverSeconds(refreshExpiration);
            this.blockRefreshToken(refreshToken);
        }

        return this.generateAuthResponse(user, evaluatedAccessExpiration, evaluatedRefreshExpiration);
    }

    private Date getAccessExpiration(String token) {
        try {
            return jwtService.extractExpiry(token);
        } catch (JwtException ex) {
            return null;
        }
    }

    private long getLeftOverSeconds(Date tokenExpiration) {
        return refreshTokenExpirySeconds - ((new Date().getTime() - tokenExpiration.getTime()) / 1000);
    }

    private boolean isNewRefreshRequired(Date issuedAt) {
        return issuedAt.before(new Date()) && (issuedAt.getTime() - new Date().getTime()) > 24 * 60 * 60 * 1000;
    }

    @Override
    public void logout(String refreshToken, String accessToken) {
        blockAccessToken(accessToken);
        blockRefreshToken(refreshToken);
    }

    @Override
    public HttpHeaders invalidateTokens() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.invalidate("at"));
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.invalidate("rt"));

        return headers;
    }

    @Override
    public void revokeAllOtherTokens(String refreshToken, String accessToken) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        userRepo.findByUsername(username).ifPresent(user -> {
            // blocking all other access tokens
            List<AccessToken> accessTokens = accessTokenRepo.findAllByUserAndIsBlockedAndTokenNot(user, false, accessToken);
            this.blockAllAccessTokens(accessTokens);

            // blocking all other refresh tokens
            List<RefreshToken> refreshTokens = refreshTokenRepo.findALLByUserAndIsBlockedAndTokenNot(user, false, refreshToken);
            this.blockAllRefreshTokens(refreshTokens);
        });
    }

    private void blockAllAccessTokens(List<AccessToken> tokens) {
        tokens.forEach(token -> {
            token.setBlocked(true);
            accessTokenRepo.save(token);
        });
    }

    private void blockAllRefreshTokens(List<RefreshToken> tokens) {
        tokens.forEach(token -> {
            token.setBlocked(true);
            refreshTokenRepo.save(token);
        });
    }

    @Override
    public void revokeAllTokens() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        userRepo.findByUsername(username).ifPresent(user -> {
            // blocking all other access tokens
            List<AccessToken> accessTokens = accessTokenRepo.findAllByUserAndIsBlocked(user, false);
            this.blockAllAccessTokens(accessTokens);

            // blocking all other refresh tokens
            List<RefreshToken> refreshTokens = refreshTokenRepo.findALLByUserAndIsBlocked(user, false);
            this.blockAllRefreshTokens(refreshTokens);
        });
    }

    /* ----------------------------------------------------------------------------------------------------------- */
    private void generateAccessToken(String username, List<String> roles, HttpHeaders headers) {
        //generating access token
        String newAccessToken = jwtService.generateAccessToken(username, roles.toString());

        // adding cookies to the HttpHeaders
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("at", newAccessToken, accessTokenExpirySeconds));

        // saving access token to the database
        // will be caching tokens in future
//        accessTokenRepo.save(AccessToken.builder()
//                .isBlocked(false)
//                .token(newAccessToken)
//                .expiration(LocalDateTime.now().plusSeconds(accessTokenExpirySeconds))
//                .user(user).build());
    }

    private void generateRefreshToken(String username, List<String> roles, HttpHeaders headers) {
        //generating access token
        String newRefreshToken = jwtService.generateRefreshToken(username, roles.toString());

        // adding cookies to the HttpHeaders
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("rt", newRefreshToken, refreshTokenExpirySeconds));

        // saving refresh token to the database
        // will be caching tokens in future
//        refreshTokenRepo.save(RefreshToken.builder()
//                .isBlocked(false)
//                .token(newRefreshToken)
//                .expiration(LocalDateTime.now().plusSeconds(refreshTokenExpirySeconds))
//                .user(user).build());
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
                .roles(user.getRoles().stream().map(UserRole::name).toList())
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

    private void sendOTPToMailId(User user, int otp) throws MessagingException {
        String text =
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Mail</title>\n" +
                        "</head>\n" +
                        "<style>\n" +
                        "    *{\n" +
                        "        font-family: Verdana, Geneva, Tahoma, sans-serif\n" +
                        "    }\n" +
                        "    h4 {\n" +
                        "        color: rgb(0, 98, 255);\n" +
                        "    }\n" +
                        "</style>\n" +
                        "<body>\n" +
                        "    <div>\n" +
                        "        <h3>Hurrey!!! You are just few steps away!</h3>\n" +
                        "        <p>Please use this OTP given below for further verification.</p>\n" +
                        "        <h4>" + otp + "</h4>\n" +
                        "        <br> <br> <br>\n" +
                        "        <p>with best regards</p>\n" +
                        "        <p>E Stores</p>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";

        mailService.sendMail(MessageData.builder()
                .to(user.getEmail())
                .subject("Verify your email for E Stores")
                .sentDate(new Date())
                .text(text).build());
    }

    private void sendConfirmationMail(User user) throws MessagingException {
        mailService.sendMail(MessageData.builder()
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
