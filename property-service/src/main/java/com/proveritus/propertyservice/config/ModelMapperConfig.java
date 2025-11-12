package com.proveritus.propertyservice.config;

import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.unit.domain.Unit;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        // Configure FloorDTO to Floor mapping
        modelMapper.typeMap(
                FloorDTO.class,
                Floor.class).addMappings(mapper -> {
            mapper.skip(Floor::setProperty);
            mapper.skip(Floor::setUnits);
        });

        // Configure UnitDTO to Unit mapping
        modelMapper.typeMap(
                UnitDTO.class,
                Unit.class).addMappings(mapper -> {
            mapper.skip(Unit::setProperty);
            mapper.skip(Unit::setFloor);
        });

        // Configure Floor to FloorDTO mapping
        modelMapper.typeMap(Floor.class, FloorDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getProperty().getId(), FloorDTO::setPropertyId);
            mapper.map(src -> src.getProperty().getPropertyType(), FloorDTO::setPropertyType);
            mapper.map(src -> src.getProperty().getAddress(), FloorDTO::setAddress);
        });

        return modelMapper;
    }
}
