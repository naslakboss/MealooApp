package codebuddies.MealooApp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueProductValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueProduct {
    String message() default "This product is already exists in database";
    Class<?>[] groups() default { } ;
    Class<? extends Payload>[] payload() default {  };
}
