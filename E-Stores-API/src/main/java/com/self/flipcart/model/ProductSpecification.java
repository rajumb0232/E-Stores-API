package com.self.flipcart.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class ProductSpecification extends Specification{
    private String productId;
}
