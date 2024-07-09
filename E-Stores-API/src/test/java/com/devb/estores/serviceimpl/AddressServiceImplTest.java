package com.devb.estores.serviceimpl;

import com.devb.estores.mapper.AddressMapper;
import com.devb.estores.model.Address;
import com.devb.estores.model.Store;
import com.devb.estores.repository.AddressRepo;
import com.devb.estores.repository.StoreRepo;
import com.devb.estores.requestdto.AddressRequest;
import com.devb.estores.responsedto.AddressResponse;
import com.devb.estores.util.ResponseStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AddressServiceImplTest {

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private StoreRepo storeRepo;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAddressToStore() {
        // Arrange
        String storeId = "123";
        AddressRequest addressRequest = new AddressRequest();
        Store store = new Store();
        store.setStoreId(storeId);

        Address address = new Address();
        AddressResponse addressResponse = new AddressResponse();

        ResponseStructure<AddressResponse> expectedResponse = new ResponseStructure<>();
        expectedResponse.setStatus(HttpStatus.CREATED.value());
        expectedResponse.setMessage("Address saved successfully");
        expectedResponse.setData(addressResponse);

        // Mock repository and mapper behavior
        when(storeRepo.findById(storeId)).thenReturn(Optional.of(store));
        when(addressMapper.mapToAddressEntity(addressRequest, address)).thenReturn(address);
        when(addressRepo.save(address)).thenReturn(address);
        when(addressMapper.mapToAddressResponse(address)).thenReturn(addressResponse);

        // Act
        ResponseEntity<ResponseStructure<AddressResponse>> responseEntity = addressServiceImpl.addAddressToStore(addressRequest, storeId);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}