package codebuddies.MealooApp.dto;

import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.validators.ProductQuantity;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ProductQuantity
public class ProductDTO {

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull(message = "Product price is mandatory")
    @Min(value = 0)
    private double price;

    private int caloriesPer100g;

    @Valid

    private Macronutrients macronutrients;

    @NotNull(message = "Product Type is mandatory")
    private ProductType productType;

    public ProductDTO(String name, double price, int caloriesPer100g, Macronutrients macronutrients, ProductType productType) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
