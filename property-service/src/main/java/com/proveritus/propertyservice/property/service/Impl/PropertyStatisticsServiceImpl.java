package com.proveritus.propertyservice.property.service.Impl;

import com.tinash.cloud.utility.enums.OccupancyStatus;
import com.tinash.cloud.utility.enums.PropertyType;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.property.dto.PropertyStatsDTO;
import com.proveritus.propertyservice.property.dto.SystemStatsDTO;
import com.proveritus.propertyservice.property.service.PropertyStatisticsService;
import com.proveritus.propertyservice.unit.domain.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PropertyStatisticsServiceImpl implements PropertyStatisticsService {

    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;

    @Override
    @Cacheable(value = "propertyStats", key = "#id")
    public PropertyStatsDTO getPropertyStats(Long id) {
        log.debug("Fetching statistics for property ID: {}", id);
        PropertyStatsDTO stats = propertyRepository.getPropertyStats(id);
        if (stats == null) {
            return new PropertyStatsDTO(0, 0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0);
        }
        return stats;
    }

    @Override
    @PreAuthorize("hasAuthority('property:count:read')")
    public long getTotalPropertiesCount() {
        log.debug("Fetching total properties count");
        return propertyRepository.countAllProperties();
    }

    @Override
    @PreAuthorize("hasAuthority('property:count:read')")
    public long countPropertiesByType(PropertyType propertyType) {
        log.debug("Counting properties by type: {}", propertyType);
        return propertyRepository.countByPropertyType(propertyType);
    }

    @Override
    @PreAuthorize("hasAuthority('system:stats:read')")
    @Cacheable(value = "systemStats", key = "'all'")
    public SystemStatsDTO getSystemWideStats() {
        log.debug("Fetching system-wide statistics");
        return calculateSystemStats(null);
    }

    @Override
    @PreAuthorize("hasAuthority('system:stats:read')")
    @Cacheable(value = "systemStats", key = "#propertyType")
    public SystemStatsDTO getSystemWideStatsByType(PropertyType propertyType) {
        log.debug("Fetching system-wide statistics for property type: {}", propertyType);
        return calculateSystemStats(propertyType);
    }

    @Override
    public PropertyStatsDTO getPropertyStatsByManager(Long managerId) {
        log.debug("Fetching statistics for properties managed by manager ID: {}", managerId);
        PropertyStatsDTO stats = propertyRepository.getPropertyStatsByManager(managerId);
        if (stats == null) {
            return new PropertyStatsDTO(0, 0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0);
        }
        return stats;
    }

    private SystemStatsDTO calculateSystemStats(PropertyType propertyType) {
        long totalProperties = propertyType == null
                ? propertyRepository.count()
                : propertyRepository.countByPropertyType(propertyType);

        long totalUnits = unitRepository.count();
        long occupiedUnits = unitRepository.countByOccupancyStatus(OccupancyStatus.OCCUPIED);

        double occupancyRate = totalUnits > 0
                ? ((double) occupiedUnits / totalUnits) * 100
                : 0.0;

        double totalActualIncome = Optional.ofNullable(unitRepository.calculateTotalActualIncome()).orElse(0.0);
        double totalPotentialIncome = Optional.ofNullable(unitRepository.calculateTotalPotentialIncome()).orElse(0.0);

        return new SystemStatsDTO(
                totalProperties,
                totalUnits,
                occupiedUnits,
                occupancyRate,
                totalActualIncome,
                totalPotentialIncome
        );
    }
}
