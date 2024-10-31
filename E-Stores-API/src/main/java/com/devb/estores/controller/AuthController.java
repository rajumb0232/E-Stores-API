package com.devb.estores.controller;

import com.devb.estores.dto.OtpModel;
import com.devb.estores.enums.UserRole;
import com.devb.estores.service.AuthService;
import com.devb.estores.util.AppResponseBuilder;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import com.devb.estores.requestdto.AuthRequest;
import com.devb.estores.requestdto.UserRequest;
import com.devb.estores.responsedto.AuthResponse;
import com.devb.estores.responsedto.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@AllArgsConstructor
@RestController
@RequestMapping("${app.base_url}")
public class AuthController extends ResponseEntityExceptionHandler {

    private final AuthService authService;
    private final AppResponseBuilder responseBuilder;

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(CsrfToken token){
        return token;
    }

    @PostMapping("/sellers/register")
    public ResponseEntity<SimpleResponseStructure> registerSeller(@RequestBody @Valid UserRequest userRequest) {
        String message = authService.registerUser(userRequest, UserRole.SELLER);
        return responseBuilder.success(HttpStatus.ACCEPTED, message);
    }

    @PostMapping("/customers/register")
    public ResponseEntity<SimpleResponseStructure> registerCustomer(@RequestBody @Valid UserRequest userRequest) {
        String message = authService.registerUser(userRequest, UserRole.CUSTOMER);
        return responseBuilder.success(HttpStatus.ACCEPTED, message);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(@RequestBody OtpModel otpModel) {
        UserResponse response = authService.verifyUserEmail(otpModel);
        return responseBuilder.success(HttpStatus.CREATED, "Account Created Successfully", response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest,
                                                                 @CookieValue(name = "rt", required = false) String refreshToken,
                                                                 @CookieValue(name = "at", required = false) String accessToken,
                                                                 @RequestHeader(value = "sec-ch-ua", required = false) String secChUa,
                                                                 @RequestHeader(value = "sec-ch-ua-platform", required = false) String secChUaPlatform,
                                                                 @RequestHeader(value = "sec-ch-ua-mobile", required = false) String secChUaMobile,
                                                                 @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        AuthResponse response = authService.login(authRequest, refreshToken, accessToken);
        HttpHeaders headers = authService.grantAccess(response, secChUa, secChUaPlatform, secChUaMobile, userAgent);
        return responseBuilder.success(HttpStatus.OK, headers, "Login Successful", response);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> logout(@CookieValue(name = "rt", required = false) String refreshToken,
                                                          @CookieValue(name = "at", required = false) String accessToken) {
        authService.logout(refreshToken, accessToken);
        HttpHeaders headers = authService.invalidateTokens();
        return responseBuilder.success(HttpStatus.OK, headers, "Logout Successful");
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken,
                                                                        @RequestHeader(value = "sec-ch-ua", required = false) String secChUa,
                                                                        @RequestHeader(value = "sec-ch-ua-platform", required = false) String secChUaPlatform,
                                                                        @RequestHeader(value = "sec-ch-ua-mobile", required = false) String secChUaMobile,
                                                                        @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        AuthResponse response = authService.refreshLogin(refreshToken, accessToken);
        HttpHeaders headers = authService.grantAccess(response, secChUa, secChUaPlatform, secChUaMobile, userAgent);
        return responseBuilder.success(HttpStatus.OK, headers, "Access Refreshed Successfully", response);
    }

    @PostMapping("/revoke-other")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken) {
        authService.revokeAllOtherTokens(refreshToken, accessToken);
        return responseBuilder.success(HttpStatus.OK, "Successfully revoked all other device access");
    }

    @PostMapping("/revoke-all")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllTokens() {
        authService.revokeAllTokens();
        HttpHeaders headers = authService.invalidateTokens();
        return responseBuilder.success(HttpStatus.OK, headers, "Successfully revoked all device access");
    }
}
