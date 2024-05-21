package com.devb.estores.requestdto;

import lombok.*;

@Getter
@Setter
public class AddressRequest {
    private String addressLine1;
    private String addressLine2;
    private String areaVillage;
    private String cityDistrict;
    private String state;
    private int pincode;
}
