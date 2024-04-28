package com.self.flipcart.mapper;

import com.self.flipcart.model.Specification;
import com.self.flipcart.responsedto.SpecificationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SpecificationMapper {

    public static List<SpecificationResponse> mapToSpecificationResponse(List<Specification> specifications) {
        return specifications.stream().map(spec -> SpecificationResponse.builder()
                .name(spec.getName())
                .value(spec.getValue())
                .build()).collect(Collectors.toList());
    }
}
