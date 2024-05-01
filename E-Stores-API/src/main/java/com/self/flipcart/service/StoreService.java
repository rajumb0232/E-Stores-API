package com.self.flipcart.service;

import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.responsedto.StoreResponse;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

public interface StoreService {
    ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(StoreRequest storeRequestComplete);

    ResponseEntity<ResponseStructure<StoreResponse>> updateStore(StoreRequest storeRequest, String storeId);


    ResponseEntity<ResponseStructure<StoreResponse>> getStore(String storeId);

    ResponseEntity<Boolean> checkIfStoreExistBySeller();

    ResponseEntity<ResponseStructure<StoreResponse>> getStoreBySeller();
}
