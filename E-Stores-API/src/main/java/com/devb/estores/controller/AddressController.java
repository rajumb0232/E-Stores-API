package com.devb.estores.controller;

import com.devb.estores.service.AddressService;
import com.devb.estores.util.AppResponseBuilder;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${app.base-url}")
public class AddressController {

    private final AddressService addressService;
    private final AppResponseBuilder responseBuilder;

    @PostMapping("/stores/{storeId}/addresses")
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddressToStore(@RequestBody AddressRequest addressRequest, @PathVariable String storeId){
        AddressResponse response = addressService.addAddressToStore(addressRequest, storeId);
        return responseBuilder.success(HttpStatus.CREATED, "Address Created", response);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(@RequestBody AddressRequest addressRequest, @PathVariable String addressId){
        AddressResponse response = addressService.updateAddress(addressRequest, addressId);
        return responseBuilder.success(HttpStatus.OK, "Address Updated", response);
    }

    @GetMapping("/addresses/addressId")
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressById(@PathVariable String addressId){
        AddressResponse response = addressService.getAddressById(addressId);
        return responseBuilder.success(HttpStatus.FOUND, "Address Found", response);
    }

    @GetMapping("/stores/{storeId}/addresses")
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressByStore(@PathVariable String storeId){
        AddressResponse response = addressService.getAddressByStore(storeId);
        return responseBuilder.success(HttpStatus.FOUND, "Address Found By Store", response);
    }
}
