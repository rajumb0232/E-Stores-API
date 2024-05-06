package com.self.flipcart.requestdto;

import com.self.flipcart.enums.MetricType;
import com.self.flipcart.model.UnitsByMetric;
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
