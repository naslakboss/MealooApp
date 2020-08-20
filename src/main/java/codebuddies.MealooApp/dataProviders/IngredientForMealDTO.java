package codebuddies.MealooApp.dataProviders;

public class IngredientForMealDTO {

    ProductForIngredientDTO product;

    int amount;

    public IngredientForMealDTO() {
    }

    public IngredientForMealDTO( ProductForIngredientDTO product, int amount) {
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
