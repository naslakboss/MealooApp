package codebuddies.MealooApp.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class IngredientForMealDTO {

    ProductForIngredientDTO product;

    @NotNull
    @Min(value = 0, message = "Ingredient amount must be greater than 0")
    int amount;

    public IngredientForMealDTO() {
    }

    public IngredientForMealDTO(ProductForIngredientDTO product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ProductForIngredientDTO getProduct() {
        return product;
    }

    public void setProduct(ProductForIngredientDTO product) {
        this.product = product;
    }
}
