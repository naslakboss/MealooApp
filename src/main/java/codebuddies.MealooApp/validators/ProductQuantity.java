package codebuddies.MealooApp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductQuantityValidator.class)
public @interface ProductQuantity {
    String message() default "\"Total sum of Macronutrients in 100g of product\" +\n" +
            "                    \" cannot exceed 100g\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
