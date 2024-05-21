package com.devb.estores.service;

import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.StoreRequest;
import com.devb.estores.responsedto.StoreResponse;
import org.springframework.http.ResponseEntity;

public interface StoreService {
    ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(StoreRequest storeRequestComplete);

    ResponseEntity<ResponseStructure<StoreResponse>> updateStore(StoreRequest storeRequest, String storeId);


    ResponseEntity<ResponseStructure<StoreResponse>> getStore(String storeId);

    ResponseEntity<Boolean> checkIfStoreExistBySeller();

    ResponseEntity<ResponseStructure<StoreResponse>> getStoreBySeller();
}
