package com.self.flipcart.responsedto;

import com.self.flipcart.enums.TopCategory;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreCardResponse implements StoreResponse{
    private String storeId;
    private String storeName;
    private TopCategory category;
    private String logoLink;
}
