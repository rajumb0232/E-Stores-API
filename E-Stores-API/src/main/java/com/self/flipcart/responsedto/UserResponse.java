package com.self.flipcart.responsedto;

import com.self.flipcart.enums.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private boolean isEmailVerified;
    private UserRole userRole;
}
