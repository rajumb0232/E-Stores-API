package com.self.flipcart.responsedto;

import com.self.flipcart.enums.SubCategory;
import com.self.flipcart.enums.TopCategory;
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
