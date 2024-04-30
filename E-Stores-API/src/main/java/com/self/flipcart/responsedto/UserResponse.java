package com.self.flipcart.responsedto;

import lombok.*;

import java.util.List;

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
    private List<String> roles;
}
