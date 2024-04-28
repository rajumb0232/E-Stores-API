package com.self.flipcart.responsedto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecificationResponse {
    private String name;
    private String value;
}
