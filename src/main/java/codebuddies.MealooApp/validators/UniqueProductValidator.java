package codebuddies.MealooApp.validators;

import codebuddies.MealooApp.dataproviders.ProductProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueProductValidator implements ConstraintValidator<UniqueProduct, String> {

    @Autowired
    ProductProvider productProvider;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return productProvider.existsByName(name);
    }

    //todo fix validator throwing null pointer exception
}
