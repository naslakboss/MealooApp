package codebuddies.MealooApp.validators;

import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.product.Product;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProductQuantityValidator implements ConstraintValidator<ProductQuantity, ProductDTO> {

    @Override
    public boolean isValid(ProductDTO productDTO, ConstraintValidatorContext context) {
        if(productDTO.getMacronutrients().getProteinsPer100g() + productDTO.getMacronutrients().getCarbohydratesPer100g() > 100
                || productDTO.getMacronutrients().getProteinsPer100g() + productDTO.getMacronutrients().getFatsPer100g() > 100
                || productDTO.getMacronutrients().getCarbohydratesPer100g() + productDTO.getMacronutrients().getFatsPer100g() > 100
                || productDTO.getMacronutrients().getProteinsPer100g() + productDTO.getMacronutrients().getCarbohydratesPer100g()
                + productDTO.getMacronutrients().getFatsPer100g() > 100){
            return false;
        }
        else return true;
    }
}
