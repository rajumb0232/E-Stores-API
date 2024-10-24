package com.devb.estores.service;

import com.devb.estores.dto.OtpModel;
import com.devb.estores.enums.UserRole;
import com.devb.estores.requestdto.AuthRequest;
import com.devb.estores.requestdto.UserRequest;
import com.devb.estores.responsedto.AuthResponse;
import com.devb.estores.responsedto.UserResponse;
import org.springframework.http.HttpHeaders;

public interface AuthService {
    String registerUser(UserRequest userRequest, UserRole role);

    UserResponse verifyUserEmail(OtpModel otpModel);

    AuthResponse login(AuthRequest authRequest, String refreshToken, String accessToken);

    HttpHeaders grantAccess(AuthResponse authResponse);

    void logout(String refreshToken, String accessToken);

    HttpHeaders invalidateTokens();

    AuthResponse refreshLogin(String refreshToken, String accessToken);

    void revokeAllOtherTokens(String refreshToken, String accessToken);

    void revokeAllTokens();
}
