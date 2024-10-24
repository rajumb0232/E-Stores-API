package com.devb.estores.service;

import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;

public interface AddressService {
    AddressResponse addAddressToStore(AddressRequest addressRequest, String storeId);

    AddressResponse updateAddress(AddressRequest addressRequest, String addressId);

    AddressResponse getAddressById(String addressId);

    AddressResponse getAddressByStore(String storeId);
}
