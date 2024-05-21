package com.devb.estores.responsedto;

import com.devb.estores.enums.TopCategory;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StorePageResponse implements StoreResponse{
    private String storeId;
    private String storeName;
    private TopCategory category;
    private String logoLink;
    private String about;
    private AddressResponse address;
}
