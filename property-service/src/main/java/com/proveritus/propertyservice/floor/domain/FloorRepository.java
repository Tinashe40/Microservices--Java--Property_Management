package com.proveritus.propertyservice.floor.domain;

import com.tinash.cloud.utility.jpa.BaseDao;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

@Repository
public interface FloorRepository extends BaseDao<Floor, String> {
    List<Floor> findByPropertyId(String propertyId);
    Page<Floor> findByPropertyId(String propertyId, Pageable pageable);
    Optional<Floor> findByNameAndPropertyId(String name, String propertyId);

    @Query("SELECT COUNT(f) FROM Floor f WHERE f.property.id = :propertyId")
    long countByPropertyId(@Param("propertyId") String propertyId);
}
