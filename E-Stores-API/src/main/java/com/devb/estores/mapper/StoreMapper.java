package com.devb.estores.mapper;

import com.devb.estores.model.Store;
import com.devb.estores.requestdto.StoreRequest;
import com.devb.estores.responsedto.StoreCardResponse;
import com.devb.estores.responsedto.StorePageResponse;
import com.devb.estores.responsedto.StoreResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StoreMapper {

    private AddressMapper addressMapper;

    public StoreResponse mapToStorePageResponse(Store store) {
        return StorePageResponse.builder()
                .storeName(store.getStoreName())
                .storeId(store.getStoreId())
                .category(store.getTopCategory())
                .logoLink(store.getLogoLink())
                .about(store.getAbout())
                .address(addressMapper.mapToAddressResponse(store.getAddress()))
                .build();
    }

    public Store mapToStoreEntity(StoreRequest storeRequest, Store store) {
        store.setStoreName(storeRequest.getStoreName());
        store.setAbout(storeRequest.getAbout());
        store.setTopCategory(storeRequest.getCategory());
        return store;
    }

    public StoreCardResponse mapToStoreCardResponse(Store store){
        return StoreCardResponse.builder()
                .storeName(store.getStoreName())
                .storeId(store.getStoreId())
                .category(store.getTopCategory())
                .logoLink(store.getLogoLink())
                .build();
    }
}
