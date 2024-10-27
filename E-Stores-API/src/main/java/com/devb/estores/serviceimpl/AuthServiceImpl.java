package com.devb.estores.serviceimpl;

import com.devb.estores.cache.CacheStore;
import com.devb.estores.dto.MessageData;
import com.devb.estores.dto.OtpModel;
import com.devb.estores.enums.UserRole;
import com.devb.estores.exceptions.*;
import com.devb.estores.model.User;
import com.devb.estores.repository.UserRepo;
import com.devb.estores.requestdto.AuthRequest;
import com.devb.estores.requestdto.UserRequest;
import com.devb.estores.responsedto.AuthResponse;
import com.devb.estores.responsedto.UserResponse;
import com.devb.estores.security.JwtService;
import com.devb.estores.service.AuthService;
import com.devb.estores.util.CookieManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final CacheStore<OtpModel> otpCache;
    private final CacheStore<User> userCacheStore;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieManager cookieManager;
    private final Random random;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;

    public AuthServiceImpl(UserRepo userRepo,
                           PasswordEncoder passwordEncoder,
                           MailService mailService,
                           CacheStore<OtpModel> otpCache, CacheStore<User> userCacheStore,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           CookieManager cookieManager,
                           Random random,
                           @Value("${token.expiry.access.seconds}") long accessTokenExpirySeconds,
                           @Value("${token.expiry.refresh.seconds}") long refreshTokenExpirySeconds) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.otpCache = otpCache;
        this.userCacheStore = userCacheStore;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.cookieManager = cookieManager;
        this.random = random;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

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
    public HttpHeaders grantAccess(AuthResponse authResponse, String secChUa, String secChUaPlatform, String secChUaMobile, String userAgent) {
        HttpHeaders headers = new HttpHeaders();

        /* Finding the start and end position of the browser name
         * */
        int start = secChUa.indexOf('"') + 1;
        int end = secChUa.indexOf("\"", start);
        String browserName = Optional.of(secChUa).map(chUa -> chUa.substring(start, end)).orElse(null);
        Map<String, Object> claims = jwtService.generateClaims(authResponse.getRoles(), browserName, secChUaPlatform, secChUaMobile, "", );

        if (authResponse.getAccessExpiration() == accessTokenExpirySeconds)
            generateAccessToken(authResponse.getUsername(), claims, headers);

        if (authResponse.getRefreshExpiration() == refreshTokenExpirySeconds)
            generateRefreshToken(authResponse.getUsername(), claims, headers);

        return headers;
    }

    @Override
    public AuthResponse refreshLogin(String refreshToken, String accessToken) {
        if (refreshToken == null) throw new UserNotLoggedInException(FAILED_REFRESH);

        Claims claims = jwtService.extractClaims(refreshToken);
        String username = jwtService.getUsername(claims);
        Date refreshExpiration = jwtService.getExpiry(claims);
        Date accessExpiration = accessToken != null ? this.getAccessExpiration(accessToken) : null;

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FAILED_REFRESH));

        /* Calculating new Access and refresh Expiration if the access token is present,
        if not present or not valid the accessExpiration stays null, in that case the default expiration
        time will be used for the token.
         */
        long evaluatedAccessExpiration = accessTokenExpirySeconds;
        long evaluatedRefreshExpiration = refreshTokenExpirySeconds;
        if (accessExpiration != null) {
            evaluatedAccessExpiration = this.getLeftOverSeconds(accessTokenExpirySeconds, accessExpiration);
            evaluatedRefreshExpiration = this.getLeftOverSeconds(refreshTokenExpirySeconds, refreshExpiration);
        }

        return this.generateAuthResponse(user, evaluatedAccessExpiration, evaluatedRefreshExpiration);
    }

    private Date getAccessExpiration(String token) {
        try {
            Claims claims = jwtService.extractClaims(token);
            return jwtService.getExpiry(claims);
        } catch (JwtException ex) {
            return null;
        }
    }

    private long getLeftOverSeconds(long expiryDuration, Date tokenExpiration) {
        return expiryDuration - ((new Date().getTime() - tokenExpiration.getTime()) / 1000);
    }

    @Override
    public void logout(String refreshToken, String accessToken) {
        // should drop old token session IDs
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
            // should drop old token session IDs
        });
    }

    @Override
    public void revokeAllTokens() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        userRepo.findByUsername(username).ifPresent(user -> {
            // should drop old token session IDs
        });
    }

    /* ----------------------------------------------------------------------------------------------------------- */
    private void generateAccessToken(String username, Map<String, Object> claims, HttpHeaders headers) {
        String newAccessToken = jwtService.generateAccessToken(username, claims);
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("at", newAccessToken, accessTokenExpirySeconds));
    }

    private void generateRefreshToken(String username, Map<String, Object> claims, HttpHeaders headers) {
        String newRefreshToken = jwtService.generateRefreshToken(username, claims);
        headers.add(HttpHeaders.SET_COOKIE, cookieManager.configure("rt", newRefreshToken, refreshTokenExpirySeconds));
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
