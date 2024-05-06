package com.self.flipcart.responsedto;

import lombok.*;


@Getter
@Setter
public class BaseProductResponse implements ProductResponse{
    private String productId;
    private String title;
    private double price;
    private int stockQuantity;
    private String availabilityStatus;
    private int totalOrders;
    private int totalReviews;
    private float avgRating;
}
