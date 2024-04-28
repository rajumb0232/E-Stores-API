package com.self.flipcart.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "variants")
public class Variant {

    @MongoId
    private String variantId;
    private List<Specification> specifications;
}
