package com.devb.estores.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class VaryingProduct extends Product{
    private Set<String> variantBy;

    @DBRef(lazy = true)
    private Set<Variant> variants;
}
