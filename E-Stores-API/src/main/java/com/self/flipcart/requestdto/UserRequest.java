package com.self.flipcart.requestdto;

import com.self.flipcart.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String password;
    @NotNull(message = "UserRole cannot be null. Please provide the userRole.")
    private UserRole userRole;
}
