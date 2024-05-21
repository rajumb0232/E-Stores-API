package com.devb.estores.dto;

import lombok.*;

import java.time.Month;
import java.time.Year;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReport {
    private String productId;
    private String productTitle;
    private Month month;
    private Year year;
    private int totalOrders;
    private int totalReturns;
    private double totalRevenue;
}
