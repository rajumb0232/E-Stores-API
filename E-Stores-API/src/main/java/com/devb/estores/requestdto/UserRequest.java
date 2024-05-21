package com.devb.estores.requestdto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String password;
}
