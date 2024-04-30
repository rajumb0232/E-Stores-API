package com.self.flipcart.service;

import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest, Class<?> userType);

    ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(OtpModel otpModel);

    ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest, String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> logout(String refreshToken, String accessToken);

    ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> revokeAllTokens();
}
