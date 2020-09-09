package codebuddies.MealooApp.config.converter;

import codebuddies.MealooApp.entities.product.ProductType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverterForProductType implements Converter<String, ProductType> {
    @Override
    public ProductType convert(String source) {
        return ProductType.valueOf(source.toUpperCase());
    }
}
