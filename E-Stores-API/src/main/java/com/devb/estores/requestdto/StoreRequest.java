package com.devb.estores.requestdto;

import com.devb.estores.enums.TopCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    private String storeName;
    private TopCategory category;
    private String about;
}
