package com.proveritus.cloudutility.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : attribute.toLowerCase();
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}