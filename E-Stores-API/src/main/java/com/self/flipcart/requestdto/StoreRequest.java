package com.self.flipcart.requestdto;

import com.self.flipcart.enums.TopCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class StoreRequest {
    private String storeName;
    private TopCategory category;
    private String about;
}
