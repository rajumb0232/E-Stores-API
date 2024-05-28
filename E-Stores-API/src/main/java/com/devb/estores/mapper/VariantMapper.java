package com.devb.estores.mapper;

import com.devb.estores.model.Variant;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class VariantMapper {

    public List<VariantResponse> mapToVariantResponseList(Set<Variant> variants) {
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
     * @return Variant,
     * (if the provided method parameter is null returns an empty ArrayList)
     */
    public Variant mapToVariantEntity(VariantRequest variantRequest) {
        return Variant.builder()
                .price(variantRequest.getPrice())
                .specifications(variantRequest.getSpecifications())
                .metricType(variantRequest.getMetricType())
                .unitsAvailableByMetric(variantRequest.getUnitsAvailableByMetric())
                .build();
    }
}
