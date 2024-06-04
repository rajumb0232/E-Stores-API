package com.devb.estores.serviceimpl;

import com.devb.estores.mapper.AddressMapper;
import com.devb.estores.model.Address;
import com.devb.estores.repository.AddressRepo;
import com.devb.estores.repository.StoreRepo;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;
import com.devb.estores.service.AddressService;
import com.devb.estores.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final StoreRepo storeRepo;
    private AddressMapper addressMapper;

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddressToStore(AddressRequest addressRequest, String storeId) {
        return storeRepo.findById(storeId).map(store -> {
            Address address = addressMapper.mapToAddressEntity(addressRequest, new Address());
            address.setContacts(new ArrayList<>());
            address = addressRepo.save(address);
            store.setAddress(address);
            storeRepo.save(store);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseStructure<AddressResponse>().setStatus(HttpStatus.CREATED.value())
                            .setMessage("Address saved successfully")
                            .setData(addressMapper.mapToAddressResponse(address)));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(AddressRequest addressRequest, String addressId) {
        return addressRepo.findById(addressId).map(address -> {
            address = addressRepo.save(addressMapper.mapToAddressEntity(addressRequest, address));
            return ResponseEntity.ok(new ResponseStructure<AddressResponse>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("successfully updated address")
                    .setData(addressMapper.mapToAddressResponse(address)));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressById(String addressId) {
        return addressRepo.findById(addressId).map(address -> ResponseEntity
                .status(HttpStatus.FOUND)
                .body(new ResponseStructure<AddressResponse>()
                        .setStatus(HttpStatus.FOUND.value())
                        .setMessage("Address found")
                        .setData(addressMapper.mapToAddressResponse(address)))).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> getAddressByStore(String storeId) {
        return storeRepo.findAddressByStoreId(storeId).map(address -> ResponseEntity
                .status(HttpStatus.FOUND)
                .body(new ResponseStructure<AddressResponse>()
                        .setStatus(HttpStatus.FOUND.value())
                        .setMessage("Address found")
                        .setData(addressMapper.mapToAddressResponse(address)))).orElseThrow();
    }


}
