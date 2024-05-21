package com.devb.estores.responsedto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
public class SimpleProductResponse extends BaseProductResponse {
    private String description;
    private CategoryResponse category;
    private StoreResponse store;

    private Map<String, String> specifications;
    // Refers to the Reviews
    // Refers to the Questions
}

