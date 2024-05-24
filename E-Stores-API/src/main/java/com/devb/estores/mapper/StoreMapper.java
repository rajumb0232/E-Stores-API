package com.devb.estores.mapper;

import com.devb.estores.model.Store;
import com.devb.estores.requestdto.StoreRequest;
import com.devb.estores.responsedto.StoreCardResponse;
import com.devb.estores.responsedto.StorePageResponse;
import com.devb.estores.responsedto.StoreResponse;

public class StoreMapper {

    private StoreMapper() {
        /*
         * Created private constructor to avoid Instantiation of class
         * */
    }

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
