package com.self.flipcart.responsedto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class VaryingProductResponse extends SimpleProductResponse{
    private boolean variable;
    private Set<String> variantBy;

    private List<VariantResponse> variants;
}
