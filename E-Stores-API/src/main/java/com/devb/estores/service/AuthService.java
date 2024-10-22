package com.devb.estores.service;

import com.devb.estores.dto.OtpModel;
import com.devb.estores.enums.UserRole;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import com.devb.estores.requestdto.AuthRequest;
import com.devb.estores.requestdto.UserRequest;
import com.devb.estores.responsedto.AuthResponse;
import com.devb.estores.responsedto.UserResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    UserResponse registerUser(UserRequest userRequest, UserRole role);

    UserResponse verifyUserEmail(OtpModel otpModel);

    AuthResponse login(AuthRequest authRequest, String refreshToken, String accessToken);

    boolean logout(String refreshToken, String accessToken);

    AuthResponse refreshLogin(String refreshToken, String accessToken);

    boolean revokeAllOtherTokens(String refreshToken, String accessToken);

    boolean revokeAllTokens();
}
