package com.tinash.propertyservice.property.domain;

import com.tinash.cloud.utility.enums.PropertyType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    public static Specification<Property> withDynamicQuery(
            String name,
            String address,
            PropertyType propertyType,
            Integer minFloors,
            Integer maxFloors,
            Integer minUnits,
            Integer maxUnits) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (address != null && !address.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + address.toLowerCase() + "%"));
            }

            if (propertyType != null) {
                predicates.add(criteriaBuilder.equal(root.get("propertyType"), propertyType));
            }

            if (minFloors != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfFloors"), minFloors));
            }

            if (maxFloors != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("numberOfFloors"), maxFloors));
            }

            if (minUnits != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfUnits"), minUnits));
            }

            if (maxUnits != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("numberOfUnits"), maxUnits));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
