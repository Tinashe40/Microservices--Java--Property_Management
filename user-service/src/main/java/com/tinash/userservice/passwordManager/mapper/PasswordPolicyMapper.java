package com.tinash.userservice.passwordManager.mapper;

import com.tinash.userservice.domain.model.password.PasswordPolicy;
import com.tinash.userservice.passwordManager.dto.PasswordPolicyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PasswordPolicyMapper {

    @Mapping(source = "passwordHistoryCount", target = "passwordHistorySize")
    PasswordPolicyDto toDto(PasswordPolicy entity);

    @Mapping(source = "passwordHistorySize", target = "passwordHistoryCount")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(PasswordPolicyDto dto,
                             @MappingTarget PasswordPolicy entity
    );
}
