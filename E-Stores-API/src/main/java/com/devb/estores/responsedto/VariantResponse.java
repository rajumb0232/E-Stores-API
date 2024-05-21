package com.devb.estores.responsedto;

import com.devb.estores.enums.MetricType;
import com.devb.estores.model.UnitsByMetric;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantResponse {
    private String variantId;
    private double price;

    private Map<String, String> specifications;
    private MetricType metricType;
    private Set<UnitsByMetric> unitsAvailableByMetric;
}
