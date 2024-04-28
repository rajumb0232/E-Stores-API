package com.self.flipcart.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreReport {
    private Month month;
    private Year year;
    private int ordersCompleted;
    private int ordersReturned;
    private double totalRevenue;

    private List<ProductReport> ProductReports;
}
