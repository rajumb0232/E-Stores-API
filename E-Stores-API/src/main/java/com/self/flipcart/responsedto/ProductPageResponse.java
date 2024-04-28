package com.self.flipcart.responsedto;

import com.self.flipcart.model.Specification;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductPageResponse implements ProductResponse {
    private String productId;
    private String productTitle;
    private String description;
    private double productPrice;
    private int productQuantity;
    private String availabilityStatus;
    private int totalOrders;
    private int totalReviews;
    private float avgRating;
    private CategoryResponse category;
    private StoreResponse store;

    private List<Specification> specification;
    // Refers to the Reviews
    // Refers to the Questions
}

