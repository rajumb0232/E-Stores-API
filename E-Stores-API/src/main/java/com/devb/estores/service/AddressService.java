package com.devb.estores.service;

import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;
import org.springframework.http.ResponseEntity;

public interface AddressService {
    AddressResponse addAddressToStore(AddressRequest addressRequest, String storeId);

    AddressResponse updateAddress(AddressRequest addressRequest, String addressId);

    AddressResponse getAddressById(String addressId);

    AddressResponse getAddressByStore(String storeId);
}
