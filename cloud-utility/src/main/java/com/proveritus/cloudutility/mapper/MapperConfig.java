package com.proveritus.cloudutility.mapper;

import org.mapstruct.ReportingPolicy;

/**
 * Global MapStruct configuration.
 */
@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapperConfig {
}
