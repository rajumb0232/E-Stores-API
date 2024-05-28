package com.devb.estores.mapper;

import com.devb.estores.model.Variant;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VariantMapper {

    private VariantMapper() {
        /*
         * Created private constructor to avoid Instantiation of class
         * */
    }

    public static List<VariantResponse> mapToVariantResponseList(Set<Variant> variants) {
        return variants != null ?
                variants.stream().map(variant -> VariantResponse.builder()
                        .variantId(variant.getVariantId())
                        .price(variant.getPrice())
                        .metricType(variant.getMetricType())
                        .specifications(variant.getSpecifications())
                        .unitsAvailableByMetric(variant.getUnitsAvailableByMetric())
                        .build()).toList()
                : new ArrayList<>();
    }

    /**
     * Maps fields such as variantPrice, specifications, metricType and unitsAvailableMetric
     * Doesn't map the product field
     *
     * @return List of variant,
     * (if the provided method parameter is null returns an empty ArrayList)
     */
    public static Variant mapToVariantEntity(VariantRequest variantRequest) {
        return Variant.builder()
                .price(variantRequest.getPrice())
                .specifications(variantRequest.getSpecifications())
                .metricType(variantRequest.getMetricType())
                .unitsAvailableByMetric(variantRequest.getUnitsAvailableByMetric())
                .build();
    }
}
