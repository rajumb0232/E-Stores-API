package com.devb.estores.requestdto;

import com.devb.estores.enums.MetricType;
import com.devb.estores.model.UnitsByMetric;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class VariantRequest {
    private String variantId;
    private double price;

    private Map<String, String> specifications;
    private MetricType metricType;
    private Set<UnitsByMetric> unitsAvailableByMetric;
}
