package codebuddies.MealooApp.config;

import codebuddies.MealooApp.entities.ProductType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverterForProductType implements Converter<String, ProductType> {
    @Override
    public ProductType convert(String source) {
        return ProductType.valueOf(source.toUpperCase());
    }
}
