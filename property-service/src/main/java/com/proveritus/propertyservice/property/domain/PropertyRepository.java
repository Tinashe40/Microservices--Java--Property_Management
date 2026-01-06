package com.proveritus.propertyservice.property.domain;

import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.jpa.BaseDao;
import com.proveritus.propertyservice.property.dto.PropertyStatsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends BaseDao<Property, String>, JpaSpecificationExecutor<Property> {
    Optional<Property> findByName(String name);

    List<Property> findByNameContainingIgnoreCase(String name);

    Page<Property> findByNameContainingIgnoreCase(String name, Pageable pageable);

//   List<Property> findByPropertyType(PropertyType propertyType);

    Page<Property> findByPropertyType(PropertyType propertyType, Pageable pageable);

    Page<Property> findByPropertyTypeAndManagedBy(PropertyType propertyType, String managedBy, Pageable pageable);

    Page<Property> findByManagedBy(String managedBy, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.address) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Property> searchProperties(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.managedBy = :managerId AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.address) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Property> searchPropertiesByManager(@Param("query") String query, @Param("managerId") String managerId, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Property p")
    long countAllProperties();

    long countByPropertyType(PropertyType propertyType);

    boolean existsByName(String name);

    @Query("""
            SELECT new com.proveritus.propertyservice.property.dto.PropertyStatsDTO(
                (SELECT COUNT(f) FROM Floor f WHERE f.property.id = p.id),
                (SELECT COUNT(u) FROM Unit u WHERE u.property.id = p.id),
                COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.OCCUPIED THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.AVAILABLE THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.RESERVED THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.NOT_AVAILABLE THEN 1 ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.UNDER_MAINTENANCE THEN 1 ELSE 0 END), 0),
                CASE WHEN (SELECT COUNT(u) FROM Unit u WHERE u.property.id = p.id) > 0 THEN (COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.OCCUPIED THEN 1 ELSE 0 END), 0) * 100.0) / (SELECT COUNT(u) FROM Unit u WHERE u.property.id = p.id) ELSE 0.0 END,
                CASE WHEN (SELECT COUNT(u) FROM Unit u WHERE u.property.id = p.id) > 0 THEN (COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.AVAILABLE THEN 1 ELSE 0 END), 0) * 100.0) / (SELECT COUNT(u) FROM Unit u WHERE u.property.id = p.id) ELSE 0.0 END,
                COALESCE(SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.OCCUPIED THEN u.monthlyRent ELSE 0 END), 0.0),
                COALESCE(SUM(u.monthlyRent), 0.0)
            )
            FROM Property p
            LEFT JOIN Unit u ON u.property.id = p.id
            WHERE p.id = :id
            GROUP BY p.id
            """)
    PropertyStatsDTO getPropertyStats(@Param("id") String id);

    @Query("""
            SELECT new com.proveritus.propertyservice.property.dto.PropertyStatsDTO(
                SUM(CASE WHEN f.id IS NOT NULL THEN 1 ELSE 0 END),
                SUM(CASE WHEN u.id IS NOT NULL THEN 1 ELSE 0 END),
                SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.OCCUPIED THEN 1 ELSE 0 END),
                SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.AVAILABLE THEN 1 ELSE 0 END),
                SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.RESERVED THEN 1 ELSE 0 END),
                SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.NOT_AVAILABLE THEN 1 ELSE 0 END),
                SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.UNDER_MAINTENANCE THEN 1 ELSE 0 END),
                CASE WHEN COUNT(u) > 0 THEN (SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.OCCUPIED THEN 1 ELSE 0 END) * 100.0) / COUNT(u) ELSE 0.0 END,
                CASE WHEN COUNT(u) > 0 THEN (SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.AVAILABLE THEN 1 ELSE 0 END) * 100.0) / COUNT(u) ELSE 0.0 END,
                SUM(CASE WHEN u.occupancyStatus = com.proveritus.cloudutility.enums.OccupancyStatus.OCCUPIED THEN u.monthlyRent ELSE 0 END),
                SUM(u.monthlyRent)
            )
            FROM Property p
            LEFT JOIN Floor f ON f.property.id = p.id
            LEFT JOIN Unit u ON u.property.id = p.id
            WHERE p.managedBy = :managerId
            GROUP BY p.managedBy
            """)
    PropertyStatsDTO getPropertyStatsByManager(@Param("managerId") String managerId);
}
