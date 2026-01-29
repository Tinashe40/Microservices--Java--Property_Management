package com.tinash.propertyservice.application.service;

import com.tinash.cloud.utility.dto.UserDto;
import com.tinash.propertyservice.application.port.out.UserServicePort;
import com.tinash.propertyservice.domain.model.property.PropertyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for enriching properties with user details.
 * Follows SRP - only handles enrichment logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyEnrichmentService {

    private final UserServicePort userServicePort;

    public PropertyDTO enrichWithUserDetails(PropertyDTO property) {
        if (property.managedBy() == null) {
            return property;
        }

        return userServicePort.findUserById(property.managedBy())
                .map(user -> property.withManagerDetails(user))
                .orElse(property);
    }

    public Page<PropertyDTO> enrichPageWithUserDetails(Page<PropertyDTO> properties) {
        List<String> managerIds = properties.getContent().stream()
                .map(PropertyDTO::managedBy)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        if (managerIds.isEmpty()) {
            return properties;
        }

        Map<String, UserDto> userMap = userServicePort.findUsersByIds(managerIds)
                .stream()
                .collect(Collectors.toMap(
                        user -> String.valueOf(user.getId()),
                        user -> user
                ));

        return properties.map(property ->
                property.managedBy() != null && userMap.containsKey(property.managedBy())
                        ? property.withManagerDetails(userMap.get(property.managedBy()))
                        : property
        );
    }
}
