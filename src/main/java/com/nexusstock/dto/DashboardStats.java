package com.nexusstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStats {
    private long totalProducts;
    private long totalStockUnits;
    private long lowStockCount;
    private long recentActivityCount;
}
