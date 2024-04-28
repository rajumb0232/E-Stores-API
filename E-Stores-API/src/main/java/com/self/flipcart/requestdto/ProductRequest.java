package com.self.flipcart.requestdto;

import com.self.flipcart.enums.SubCategory;
import com.self.flipcart.enums.TopCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String productTitle;
    private double productPrice;
    private int stockQuantity;
    private String description;
    private TopCategory topCategory;
    private SubCategory subCategory;
    private String productType;
}
