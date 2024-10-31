package com.devb.estores.service;

import com.devb.estores.requestdto.StoreRequest;
import com.devb.estores.responsedto.StoreResponse;

public interface StoreService {
    StoreResponse setUpStore(StoreRequest storeRequestComplete);

    StoreResponse updateStore(StoreRequest storeRequest, String storeId);


    StoreResponse getStore(String storeId);

    Boolean checkIfStoreExistBySeller();

    StoreResponse getStoreBySeller();
}
