package com.self.flipcart.mapper;

import com.self.flipcart.model.Address;
import com.self.flipcart.requestdto.AddressRequest;
import com.self.flipcart.responsedto.AddressResponse;

import java.util.stream.Collectors;

public class AddressMapper {
    public static AddressResponse mapToAddressResponse(Address address) {
        if(address != null)
        return AddressResponse.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .areaVillage(address.getAreaVillage())
                .addressId(address.getAddressId())
                .cityDistrict(address.getCityDistrict())
                .state(address.getState())
                .Country(address.getCountry())
                .pincode(address.getPincode())
                .contacts(address.getContacts().stream()
                        .map(ContactMapper::mapToContactResponse)
                        .collect(Collectors.toList()))
                .build();
        else return null;
    }

    public static Address mapToAddressEntity(AddressRequest addressRequest, Address address) {
        address.setAddressLine1(addressRequest.getAddressLine1());
        address.setAddressLine2(addressRequest.getAddressLine2());
        address.setAreaVillage(addressRequest.getAreaVillage());
        address.setCityDistrict(addressRequest.getCityDistrict());
        address.setState(addressRequest.getState());
        address.setCountry("India");
        address.setPincode(addressRequest.getPincode());
        return address;
    }
}
