package com.devb.estores.mapper;

import com.devb.estores.model.Address;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;

public class AddressMapper {

    private AddressMapper() {
        /*
         * Created private constructor to avoid Instantiation of class
         * */
    }

    public static AddressResponse mapToAddressResponse(Address address) {
        return address != null ?
                AddressResponse.builder()
                        .addressLine1(address.getAddressLine1())
                        .addressLine2(address.getAddressLine2())
                        .areaVillage(address.getAreaVillage())
                        .addressId(address.getAddressId())
                        .cityDistrict(address.getCityDistrict())
                        .state(address.getState())
                        .country(address.getCountry())
                        .pincode(address.getPincode())
                        .contacts(address.getContacts().stream()
                                .map(ContactMapper::mapToContactResponse)
                                .toList())
                        .build()
                : null;
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
