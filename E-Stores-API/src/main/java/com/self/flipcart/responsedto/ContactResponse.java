package com.self.flipcart.responsedto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactResponse {
    private String contactId;
    private String contactName;
    private long contactNumber;
    private boolean isPrimary;
}
