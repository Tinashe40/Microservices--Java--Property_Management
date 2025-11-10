package com.proveritus.propertyservice.property.service.enrichment;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.propertyservice.client.UserClient;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.dto.PropertyDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEnrichmentService {

    private final UserClient userClient;

    public void enrichPropertyDTOWithUserDetails(Property property, PropertyDTO propertyDTO) {
        if (property.getManagedBy() != null) {
            try {
                UserDTO userDTO = userClient.getUserById(property.getManagedBy());
                propertyDTO.setManagedByDetails(userDTO);
            } catch (FeignException e) {
                log.error("Unable to fetch user details for user id: {}", property.getManagedBy(), e);
            }
        }
    }

    public Page<PropertyDTO> enrichPropertiesWithUserDetails(Page<Property> properties, Page<PropertyDTO> propertyDTOs) {
        List<Long> userIds = properties.getContent().stream()
                .map(Property::getManagedBy)
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return propertyDTOs;
        }

        try {
            List<UserDTO> userDTOs = userClient.getUsersByIds(userIds);
            Map<Long, UserDTO> userMap = userDTOs.stream()
                    .collect(Collectors.toMap(UserDTO::getId, user -> user));

            propertyDTOs.forEach(dto -> {
                if (dto.getManagedBy() != null) {
                    dto.setManagedByDetails(userMap.get(dto.getManagedBy()));
                }
            });

            return propertyDTOs;
        } catch (FeignException e) {
            log.error("Unable to fetch user details for user ids: {}", userIds, e);
            return propertyDTOs;
        }
    }
}
