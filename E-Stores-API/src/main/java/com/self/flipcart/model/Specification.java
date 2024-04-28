package com.self.flipcart.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "specifications")
public class Specification {
    @MongoId
    private String specificationId;
    private String name;
    private String value;

    @DBRef
    private Product product;
}
