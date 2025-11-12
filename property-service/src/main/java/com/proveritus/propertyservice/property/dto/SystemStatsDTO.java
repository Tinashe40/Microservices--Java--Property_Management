package com.proveritus.propertyservice.property.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatsDTO {
    private long totalProperties;
    private long totalUnits;
    private long occupiedUnits;
    private double overallOccupancyRate;
    private double totalActualIncome;
    private double totalPotentialIncome;
}
