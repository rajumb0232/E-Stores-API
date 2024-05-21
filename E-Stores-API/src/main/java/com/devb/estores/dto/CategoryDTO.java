package com.devb.estores.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CategoryDTO {
    private String displayName;
    private String categoryName;
    private List<String> productTypes;
}
