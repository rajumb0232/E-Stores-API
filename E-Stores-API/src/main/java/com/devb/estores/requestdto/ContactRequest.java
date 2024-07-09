package com.devb.estores.requestdto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    private String contactName;
    private long contactNumber;
    private boolean primary;
}
