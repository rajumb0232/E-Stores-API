package com.self.flipcart.serviceimpl;

import com.self.flipcart.mapper.AddressMapper;
import com.self.flipcart.model.Address;
import com.self.flipcart.repository.AddressRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.requestdto.AddressRequest;
import com.self.flipcart.responsedto.AddressResponse;
import com.self.flipcart.service.AddressService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private AddressRepo addressRepo;
    private StoreRepo storeRepo;
    private ResponseStructure<AddressResponse> structure;

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddressToStore(AddressRequest addressRequest, String storeId) {
        return storeRepo.findById(storeId).map(store -> {
            Address address = AddressMapper.mapToAddressEntity(addressRequest, new Address());
            address.setContacts(new ArrayList<>());
            address = addressRepo.save(address);
            store.setAddress(address);
            storeRepo.save(store);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseStructure<AddressResponse>().setStatus(HttpStatus.CREATED.value())
                            .setMessage("Address saved successfully")
                            .setData(AddressMapper.mapToAddressResponse(address)));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(AddressRequest addressRequest, String addressId) {
        System.out.println(addressRequest);
        return addressRepo.findById(addressId).map(address -> {
            address = addressRepo.save(AddressMapper.mapToAddressEntity(addressRequest, address));
            return ResponseEntity.ok(new ResponseStructure<AddressResponse>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("successfully updated address")
                    .setData(AddressMapper.mapToAddressResponse(address)));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressById(String addressId) {
        return addressRepo.findById(addressId).map(address -> ResponseEntity
                .status(HttpStatus.FOUND)
                .body(new ResponseStructure<AddressResponse>()
                        .setStatus(HttpStatus.FOUND.value())
                        .setMessage("Address found")
                        .setData(AddressMapper.mapToAddressResponse(address)))).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressByStore(String storeId) {
        return storeRepo.findAddressByStoreId(storeId).map(address -> ResponseEntity
                .status(HttpStatus.FOUND)
                .body(new ResponseStructure<AddressResponse>()
                        .setStatus(HttpStatus.FOUND.value())
                        .setMessage("Address found")
                        .setData(AddressMapper.mapToAddressResponse(address)))).orElseThrow();
    }


}
