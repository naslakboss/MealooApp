package codebuddies.MealooApp.config.converter;

import codebuddies.MealooApp.config.converter.StringToEnumConverterForMealDifficulty;
import codebuddies.MealooApp.config.converter.StringToEnumConverterForProductType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverterForMealDifficulty());
        registry.addConverter(new StringToEnumConverterForProductType());
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
