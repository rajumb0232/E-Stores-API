package com.devb.estores.responsedto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private String addressId;
    private String addressLine1;
    private String addressLine2;
    private String areaVillage;
    private String cityDistrict;
    private String state;
    private String country;
    private int pincode;
    private List<ContactResponse> contacts;
}
