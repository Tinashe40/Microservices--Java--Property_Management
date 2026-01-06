package com.proveritus.propertyservice.unit.dto;

import com.proveritus.cloudutility.enums.*;
import com.proveritus.cloudutility.jpa.Updatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UnitDTO implements Updatable {

    private String id;

    @NotBlank(message = "Unit name is required")
    private String name;

    @Positive(message = "Size must be positive")
    private Double size;

    private RentType rentType;

    @Positive(message = "Rate per square meter must be positive")
    private Double ratePerSqm;

    @Positive(message = "Monthly rent must be positive")
    private Double monthlyRent;

    private OccupancyStatus occupancyStatus;

    private String tenant;

    private String tenantEmail;

    private String tenantPhone;

    private LocalDate leaseStartDate;

    private LocalDate leaseEndDate;

    @NotNull(message = "Property ID is required")
    private String propertyId;

    private String floorId;
}
