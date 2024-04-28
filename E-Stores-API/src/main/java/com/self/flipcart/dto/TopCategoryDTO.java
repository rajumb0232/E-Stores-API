package com.self.flipcart.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TopCategoryDTO {
    private String displayName;
    private String topCategoryName;
    private String topCategoryImage;
    private List<CategoryDTO> categories;
}
