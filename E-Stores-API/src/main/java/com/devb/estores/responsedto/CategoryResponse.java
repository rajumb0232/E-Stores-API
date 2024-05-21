package com.devb.estores.responsedto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryResponse {
    private String topCategory;
    private String subCategory;
    private String productType;
}
