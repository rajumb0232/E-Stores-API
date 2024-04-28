package com.self.flipcart.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @MongoId
    private String productId;
    private String productTitle;
    private String description;
    private double productPrice;
    private int stockQuantity;
    private String topCategory;
    private String subCategory;

    @DBRef
    private ProductType productType;

    private String availabilityStatus;
    private int totalOrders;
    private int totalReviews;
    private float avgRating;
    // Refers to the ProductType
    private String productTypeId;
    // Refers to the Store
    private String storeId;
    // Refers to the Reviews
    // Refers to the Questions

    @DBRef(lazy = true)
    private List<Specification> specification;
}
