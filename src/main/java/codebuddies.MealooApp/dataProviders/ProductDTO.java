package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.ProductType;

public class ProductDTO {

    private String name;

    private int price;

    private int caloriesPer100g;

    private Macronutrients macronutrients;

    private ProductType productType;

    public ProductDTO() {
    }

    public ProductDTO(String name, int price) {
        this.name = name;
        this.price = price;
        this.caloriesPer100g = caloriesPer100g;
        this.macronutrients = macronutrients;
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public void setCaloriesPer100g(int caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
    }

    public Macronutrients getMacronutrients() {
        return macronutrients;
    }

    public void setMacronutrients(Macronutrients macronutrients) {
        this.macronutrients = macronutrients;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

}
