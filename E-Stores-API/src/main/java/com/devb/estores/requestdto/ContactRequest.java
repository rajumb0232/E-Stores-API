package com.devb.estores.requestdto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContactRequest {
    private String contactName;
    private long contactNumber;
    private boolean primary;
}
