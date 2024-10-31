package com.devb.estores.serviceimpl;

import com.devb.estores.mapper.AddressMapper;
import com.devb.estores.model.Address;
import com.devb.estores.model.Store;
import com.devb.estores.repository.AddressRepo;
import com.devb.estores.repository.StoreRepo;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;
import com.devb.estores.util.ResponseStructure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AddressServiceImplTest {

    @MockBean
    private AddressRepo addressRepo;

    @MockBean
    private StoreRepo storeRepo;

    @MockBean
    private AddressMapper addressMapper;

    @Autowired
    private AddressServiceImpl addressServiceImpl;

    @Test
    void addAddressToStore() {
        // Arrange
        String storeId = "123";
        AddressRequest addressRequest = new AddressRequest();
        Store store = new Store();
        store.setStoreId(storeId);

        Address address = new Address();
        AddressResponse addressResponse = new AddressResponse();

        // Mock repository and mapper behavior
        when(storeRepo.findById(storeId)).thenReturn(Optional.of(store));
        when(addressMapper.mapToAddressEntity(any(AddressRequest.class), any(Address.class))).thenReturn(address);
        when(addressRepo.save(address)).thenReturn(address);
        when(storeRepo.save(store)).thenReturn(store);
        when(addressMapper.mapToAddressResponse(address)).thenReturn(addressResponse);

        // Act
        AddressResponse response = addressServiceImpl.addAddressToStore(addressRequest, storeId);

        // Assert
        assertEquals(addressResponse, response, "The returned address Response should match the mocked addressResponse Object.");

        // Verify Repo's
        verify(addressRepo).save(address);
        verify(storeRepo).save(store);
    }
}