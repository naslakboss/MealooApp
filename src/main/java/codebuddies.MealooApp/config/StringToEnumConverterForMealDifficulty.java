package codebuddies.MealooApp.config;

import codebuddies.MealooApp.entities.MealDifficulty;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverterForMealDifficulty implements Converter<String, MealDifficulty> {

    @Override
    public MealDifficulty convert(String source) {
        return MealDifficulty.valueOf(source.toUpperCase());
    }
}
