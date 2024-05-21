package com.devb.estores.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UnitsByMetric {
    private String metricValue;
    private int unitsAvailable;
}
