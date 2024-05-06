package com.self.flipcart.mapper;

import com.self.flipcart.model.Variant;
import com.self.flipcart.requestdto.VariantRequest;
import com.self.flipcart.responsedto.VariantResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VariantMapper {
    public static List<VariantResponse> mapToVariantResponseList(Set<Variant> variants) {
        if(variants != null)
        return variants.stream().map(variant -> VariantResponse.builder()
                .variantId(variant.getVariantId())
                .price(variant.getPrice())
                .metricType(variant.getMetricType())
                .specifications(variant.getSpecifications())
                .unitsAvailableByMetric(variant.getUnitsAvailableByMetric())
                .build()).toList();

        else return new ArrayList<>();
    }

    /**
     * Maps fields such as variantPrice, specifications, metricType and unitsAvailableMetric
     * Doesn't map the product field
     *
     * @return List of variant,
     * (if the provided method parameter is null returns an empty ArrayList)
     */
    public static Variant mapToVariantEntity(VariantRequest variant) {
            return  Variant.builder()
                        .price(variant.getPrice())
                        .specifications(variant.getSpecifications())
                        .metricType(variant.getMetricType())
                        .unitsAvailableByMetric(variant.getUnitsAvailableByMetric())
                        .build();
    }
}
