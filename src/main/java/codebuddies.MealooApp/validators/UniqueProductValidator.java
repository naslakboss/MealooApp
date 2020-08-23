package codebuddies.MealooApp.validators;

import codebuddies.MealooApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueProductValidator implements ConstraintValidator<UniqueProduct, String> {

    @Autowired
    ProductRepository productRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return !productRepository.existsByName(name);
    }
}
