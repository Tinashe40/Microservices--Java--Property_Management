package com.proveritus.propertyservice.floor.dto.response;

import com.proveritus.propertyservice.unit.dto.response.UnitResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorResponse {

    private Long id;
    private Long propertyId;
    private String name;
    private String propertyType;
    private String address;
    private Integer numberOfUnits;
    private Integer occupiedUnits;
    private Integer vacantUnits;
    private List<UnitResponse> units;
}
