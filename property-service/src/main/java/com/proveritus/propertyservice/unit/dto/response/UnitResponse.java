package com.proveritus.propertyservice.unit.dto.response;

import com.proveritus.cloudutility.enums.*;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UnitResponse {

    private Long id;
    private String name;
    private Double size;
    private RentType rentType;
    private Double ratePerSqm;
    private Double monthlyRent;
    private OccupancyStatus occupancyStatus;
    private String tenant;
    private String tenantEmail;
    private String tenantPhone;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private Long propertyId;
    private Long floorId;
}
