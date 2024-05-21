package com.devb.estores.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class VaryingProductRequest extends ProductRequest{
    private Set<String> variantBy;
}
