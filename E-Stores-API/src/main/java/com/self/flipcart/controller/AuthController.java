package com.self.flipcart.controller;

import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.enums.UserRole;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.AuthService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@AllArgsConstructor
@RestController
@RequestMapping("${app.base_url}")
public class AuthController extends ResponseEntityExceptionHandler {

    private AuthService authService;

    @PostMapping("/sellers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerSeller(@RequestBody @Valid UserRequest userRequest) {
        return authService.registerUser(userRequest, UserRole.SELLER);
    }

    @PostMapping("/customers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerCustomer(@RequestBody @Valid UserRequest userRequest) {
        return authService.registerUser(userRequest, UserRole.CUSTOMER);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(@RequestBody OtpModel otpModel) {
        return authService.verifyUserEmail(otpModel);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest,
                                                                 @CookieValue(name = "rt", required = false) String refreshToken,
                                                                 @CookieValue(name = "at", required = false) String accessToken) {
        return authService.login(authRequest, refreshToken, accessToken);
    }

    @PostMapping("/logout")
//    @Secured({"ADMIN", "SUPER_ADMIN","SELLER", "CUSTOMER"})
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> logout(@CookieValue(name = "rt", required = false) String refreshToken,
                                                          @CookieValue(name = "at", required = false) String accessToken) {
        return authService.logout(refreshToken, accessToken);
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken) {
        return authService.refreshLogin(refreshToken, accessToken);
    }

    @PostMapping("/revoke-other")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken) {
        return authService.revokeAllOtherTokens(refreshToken, accessToken);
    }

    @PostMapping("/revoke-all")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllTokens() {
        return authService.revokeAllTokens();
    }
}
