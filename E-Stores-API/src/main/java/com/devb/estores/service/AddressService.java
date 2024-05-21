package com.devb.estores.service;

import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;
import org.springframework.http.ResponseEntity;

public interface AddressService {
    ResponseEntity<ResponseStructure<AddressResponse>> addAddressToStore(AddressRequest addressRequest, String storeId);

    ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(AddressRequest addressRequest, String addressId);

    ResponseEntity<ResponseStructure<AddressResponse>> getAddressById(String addressId);

    ResponseEntity<ResponseStructure<AddressResponse>> getAddressByStore(String storeId);
}
