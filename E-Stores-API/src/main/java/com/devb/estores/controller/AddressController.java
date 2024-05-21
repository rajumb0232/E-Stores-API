package com.devb.estores.controller;

import com.devb.estores.service.AddressService;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("${app.base_url}")
public class AddressController {

    private AddressService addressService;

    @PostMapping("/stores/{storeId}/addresses")
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddressToStore(@RequestBody AddressRequest addressRequest, @PathVariable String storeId){
        System.err.println(storeId);
        return addressService.addAddressToStore(addressRequest, storeId);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(@RequestBody AddressRequest addressRequest, @PathVariable String addressId){
        return addressService.updateAddress(addressRequest, addressId);
    }

    @GetMapping("/addresses/addressId")
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressById(@PathVariable String addressId){
        return addressService.getAddressById(addressId);
    }

    @GetMapping("/stores/{storeId}/addresses")
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressByStore(@PathVariable String storeId){
        return addressService.getAddressByStore(storeId);
    }
}
