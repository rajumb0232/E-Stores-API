package com.self.flipcart.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

@Getter
@Setter
//@Builder
//@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @MongoId
    private String productId;
    private String title;
    private String description;
    private boolean variable;
    private String topCategory;
    private String subCategory;
    // Refers to the ProductType
    private String productTypeId;
    // Refers to the Store
    private String storeId;
    // Refers to the Reviews
    // Refers to the Questions

    private Map<String, String> specifications;
}
