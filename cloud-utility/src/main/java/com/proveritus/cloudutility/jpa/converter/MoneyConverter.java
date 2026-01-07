package com.proveritus.cloudutility.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<MonetaryAmount, String> {

    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        return dbData == null ? null : MonetaryFormats.getAmountFormat(Locale.ROOT).parse(dbData);
    }
}