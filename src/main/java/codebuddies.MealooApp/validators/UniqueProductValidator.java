package codebuddies.MealooApp.validators;

import codebuddies.MealooApp.dataProviders.ProductFacade;
import codebuddies.MealooApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueProductValidator implements ConstraintValidator<UniqueProduct, String> {

    @Autowired
    ProductFacade productFacade;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return productFacade.existsByName(name);
    }

    //todo fix validator throwing null pointer exception
}
