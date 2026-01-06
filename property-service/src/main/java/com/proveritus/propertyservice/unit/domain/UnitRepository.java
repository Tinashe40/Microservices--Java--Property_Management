package com.proveritus.propertyservice.unit.domain;

import com.proveritus.cloudutility.jpa.BaseDao;
import com.proveritus.cloudutility.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends BaseDao<Unit, String> {
    List<Unit> findByPropertyId(String propertyId);

    List<Unit> findByFloorId(String floorId);

    Optional<Unit> findByNameAndPropertyId(String name, String propertyId);

    Page<Unit> findByPropertyId(String propertyId, Pageable pageable);

    Page<Unit> findByFloorId(String floorId, Pageable pageable);

    long countByFloorId(String floorId);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.floor.id = :floorId AND u.occupancyStatus = :occupancyStatus")
    long countByFloorIdAndOccupancy(@Param("floorId") String floorId, @Param("occupancy") OccupancyStatus occupancyStatus);

    List<Unit> findByOccupancyStatus(OccupancyStatus occupancyStatus);

    Page<Unit> findByOccupancyStatus(OccupancyStatus occupancyStatus, Pageable pageable);

    @Query("SELECT u FROM Unit u WHERE " +
            "(:propertyId IS NULL OR u.property.id = :propertyId) AND " +
            "(:floorId IS NULL OR u.floor.id = :floorId) AND " +
            "(:occupancyStatus IS NULL OR u.occupancyStatus = :occupancyStatus)")
    Page<Unit> findWithFilters(@Param("propertyId") String propertyId,
                               @Param("floorId") String floorId,
                               @Param("occupancyStatus") OccupancyStatus occupancyStatus,
                               Pageable pageable);

    @Query("SELECT u FROM Unit u WHERE " + "LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " + "LOWER(u.tenant) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Unit> searchUnits(@Param("query") String query, Pageable pageable);

    @Query("SELECT COALESCE(SUM(u.monthlyRent), 0) FROM Unit u WHERE u.property.id = :propertyId AND u.occupancyStatus = 'OCCUPIED'")
    Double calculateTotalRentalIncome(@Param("propertyId") String propertyId);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.property.id = :propertyId")
    long countByPropertyId(@Param("propertyId") String propertyId);

    long countByOccupancyStatus(OccupancyStatus occupancyStatus);

    @Query("SELECT COALESCE(SUM(u.monthlyRent), 0) FROM Unit u WHERE u.occupancyStatus = 'OCCUPIED'")
    Double calculateTotalActualIncome();

    @Query("SELECT COALESCE(SUM(u.monthlyRent), 0) FROM Unit u")
    Double calculateTotalPotentialIncome();

}
