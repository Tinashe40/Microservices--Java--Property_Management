package com.proveritus.propertyservice.property.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatsResponse {
    private long totalProperties;
    private long totalUnits;
    private long occupiedUnits;
    private double overallOccupancyRate;
    private double totalActualIncome;
    private double totalPotentialIncome;
}
