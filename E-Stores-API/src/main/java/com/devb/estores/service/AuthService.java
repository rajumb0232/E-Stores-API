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
    ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest, UserRole role);

    ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(OtpModel otpModel);

    ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest, String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> logout(String refreshToken, String accessToken);

    ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> revokeAllTokens();
}
