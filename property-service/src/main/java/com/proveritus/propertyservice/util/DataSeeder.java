package com.proveritus.propertyservice.util;

import com.proveritus.cloudutility.enums.OccupancyStatus;
import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.enums.RentType;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.floor.domain.FloorRepository;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.unit.domain.Unit;
import com.proveritus.propertyservice.unit.domain.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;
    private final UnitRepository unitRepository;

    @Override
    public void run(String... args) throws Exception {
        if (propertyRepository.count() == 0) {
            log.info("Seeding data...");
            seedData();
            log.info("Data seeding completed.");
        } else {
            log.info("Data already exists. Skipping seeding.");
        }
    }

    private void seedData() {
        // Property 1
        Property property1 = Property.builder()
                .name("Greenwood Plaza")
                .propertyType(PropertyType.COMMERCIAL)
                .address("123 Main St, Anytown, USA")
                .numberOfFloors(2)
                .numberOfUnits(10)
                .managedBy(1L)
                .build();
        propertyRepository.save(property1);

        Floor p1_floor1 = Floor.builder()
                .name("First Floor")
                .property(property1)
                .numberOfUnits(5)
                .occupiedUnits(0)
                .vacantUnits(5)
                .build();
        floorRepository.save(p1_floor1);

        List<Unit> p1_floor1_units = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            p1_floor1_units.add(Unit.builder()
                    .name("Unit 10" + i)
                    .size(100.5)
                    .rentType(RentType.FLAT)
                    .monthlyRent(1200.00)
                    .occupancyStatus(OccupancyStatus.AVAILABLE)
                    .property(property1)
                    .floor(p1_floor1)
                    .build());
        }
        unitRepository.saveAll(p1_floor1_units);

        Floor p1_floor2 = Floor.builder()
                .name("Second Floor")
                .property(property1)
                .numberOfUnits(5)
                .occupiedUnits(0)
                .vacantUnits(5)
                .build();
        floorRepository.save(p1_floor2);

        List<Unit> p1_floor2_units = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            p1_floor2_units.add(Unit.builder()
                    .name("Unit 20" + i)
                    .size(150.0)
                    .rentType(RentType.FLAT)
                    .monthlyRent(1800.00)
                    .occupancyStatus(OccupancyStatus.AVAILABLE)
                    .property(property1)
                    .floor(p1_floor2)
                    .build());
        }
        unitRepository.saveAll(p1_floor2_units);

        // Property 2
        Property property2 = Property.builder()
                .name("Sunset Apartments")
                .propertyType(PropertyType.RESIDENTIAL)
                .address("456 Oak Ave, Anytown, USA")
                .numberOfFloors(2)
                .numberOfUnits(6)
                .managedBy(1L)
                .build();
        propertyRepository.save(property2);

        Floor p2_floor1 = Floor.builder()
                .name("Ground Floor")
                .property(property2)
                .numberOfUnits(3)
                .occupiedUnits(3)
                .vacantUnits(0)
                .build();
        floorRepository.save(p2_floor1);

        List<Unit> p2_floor1_units = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            p2_floor1_units.add(Unit.builder()
                    .name("A" + i)
                    .size(80.0)
                    .rentType(RentType.FLAT)
                    .monthlyRent(900.00)
                    .occupancyStatus(OccupancyStatus.OCCUPIED)
                    .tenant("Tenant " + i)
                    .tenantEmail("tenant" + i + "@example.com")
                    .tenantPhone("123-456-789" + i)
                    .leaseStartDate(LocalDate.now().minusMonths(i))
                    .leaseEndDate(LocalDate.now().plusMonths(12 - i))
                    .property(property2)
                    .floor(p2_floor1)
                    .build());
        }
        unitRepository.saveAll(p2_floor1_units);

        Floor p2_floor2 = Floor.builder()
                .name("First Floor")
                .property(property2)
                .numberOfUnits(3)
                .occupiedUnits(0)
                .vacantUnits(3)
                .build();
        floorRepository.save(p2_floor2);

        List<Unit> p2_floor2_units = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            p2_floor2_units.add(Unit.builder()
                    .name("B" + i)
                    .size(90.0)
                    .rentType(RentType.FLAT)
                    .monthlyRent(1000.00)
                    .occupancyStatus(OccupancyStatus.AVAILABLE)
                    .property(property2)
                    .floor(p2_floor2)
                    .build());
        }
        unitRepository.saveAll(p2_floor2_units);
    }
}
