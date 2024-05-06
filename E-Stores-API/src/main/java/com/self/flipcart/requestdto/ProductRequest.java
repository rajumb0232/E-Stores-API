package com.self.flipcart.requestdto;

import com.self.flipcart.enums.SubCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProductRequest {
    private String title;
    private String description;
    // The top category is taken directly from the store
    private SubCategory subCategory;
    private String productType;
    private Map<String, String> specifications;
}
