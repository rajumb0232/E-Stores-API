package com.devb.estores.controller;

import com.devb.estores.dto.OtpModel;
import com.devb.estores.enums.UserRole;
import com.devb.estores.service.AuthService;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import com.devb.estores.requestdto.AuthRequest;
import com.devb.estores.requestdto.UserRequest;
import com.devb.estores.responsedto.AuthResponse;
import com.devb.estores.responsedto.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@AllArgsConstructor
@RestController
@RequestMapping("${app.base_url}")
public class AuthController extends ResponseEntityExceptionHandler {

    private AuthService authService;

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(CsrfToken token){
        return token;
    }

    @PostMapping("/sellers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerSeller(@RequestBody @Valid UserRequest userRequest) {
        UserResponse response = authService.registerUser(userRequest, UserRole.SELLER);
    }

    @PostMapping("/customers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerCustomer(@RequestBody @Valid UserRequest userRequest) {
        UserResponse response = authService.registerUser(userRequest, UserRole.CUSTOMER);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(@RequestBody OtpModel otpModel) {
        UserResponse response = authService.verifyUserEmail(otpModel);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest,
                                                                 @CookieValue(name = "rt", required = false) String refreshToken,
                                                                 @CookieValue(name = "at", required = false) String accessToken) {
        AuthResponse response = authService.login(authRequest, refreshToken, accessToken);
    }

    @PostMapping("/logout")
//    @Secured({"ADMIN", "SUPER_ADMIN","SELLER", "CUSTOMER"})
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> logout(@CookieValue(name = "rt", required = false) String refreshToken,
                                                          @CookieValue(name = "at", required = false) String accessToken) {
        boolean result = authService.logout(refreshToken, accessToken);
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken) {
        AuthResponse response = authService.refreshLogin(refreshToken, accessToken);
    }

    @PostMapping("/revoke-other")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken) {
        boolean result = authService.revokeAllOtherTokens(refreshToken, accessToken);
    }

    @PostMapping("/revoke-all")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllTokens() {
        boolean result = authService.revokeAllTokens();
    }
}
