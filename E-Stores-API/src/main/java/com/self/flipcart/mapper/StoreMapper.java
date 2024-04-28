package com.self.flipcart.mapper;

import com.self.flipcart.model.Store;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.responsedto.StoreCardResponse;
import com.self.flipcart.responsedto.StorePageResponse;
import com.self.flipcart.responsedto.StoreResponse;

public class StoreMapper {

    public static StoreResponse mapToStorePageResponse(Store store) {
        return StorePageResponse.builder()
                .storeName(store.getStoreName())
                .storeId(store.getStoreId())
                .category(store.getTopCategory())
                .logoLink(store.getLogoLink())
                .about(store.getAbout())
                .address(AddressMapper.mapToAddressResponse(store.getAddress()))
                .build();
    }

    public static Store mapToStoreEntity(StoreRequest storeRequest, Store store) {
        store.setStoreName(storeRequest.getStoreName());
        store.setAbout(storeRequest.getAbout());
        store.setTopCategory(storeRequest.getCategory());
        return store;
    }

    public static StoreCardResponse mapToStoreCardResponse(Store store){
        return StoreCardResponse.builder()
                .storeName(store.getStoreName())
                .storeId(store.getStoreId())
                .category(store.getTopCategory())
                .logoLink(store.getLogoLink())
                .build();
    }
}
