package com.self.flipcart.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class SimpleProduct extends Product{
    private int stockQuantity;
    private double price;
}
