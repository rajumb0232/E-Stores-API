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
    public AddressResponse addAddressToStore(AddressRequest addressRequest, String storeId) {
        return storeRepo.findById(storeId).map(store -> {
            Address address = addressMapper.mapToAddressEntity(addressRequest, new Address());
            address.setContacts(new ArrayList<>());
            address = addressRepo.save(address);
            store.setAddress(address);
            storeRepo.save(store);

            return addressMapper.mapToAddressResponse(address);
        }).orElseThrow();
    }

    @Override
    public AddressResponse updateAddress(AddressRequest addressRequest, String addressId) {
        return addressRepo.findById(addressId).map(address -> {
            addressMapper.mapToAddressEntity(addressRequest, address);
            address = addressRepo.save(address);
            return addressMapper.mapToAddressResponse(address);
        }).orElseThrow();
    }

    @Override
    public AddressResponse getAddressById(String addressId) {
        return addressRepo.findById(addressId)
                .map(addressMapper::mapToAddressResponse)
                .orElseThrow();
    }

    @Override
    public AddressResponse getAddressByStore(String storeId) {
        return storeRepo.findAddressByStoreId(storeId)
                .map(addressMapper::mapToAddressResponse)
                .orElseThrow();
    }


}
