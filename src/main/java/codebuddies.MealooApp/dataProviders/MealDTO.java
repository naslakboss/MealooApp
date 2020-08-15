package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.product.Macronutrients;

import java.util.List;

public class MealDTO {

    String name;

    List<ProductForMealDTO> products;

    int price;

    MealDifficulty mealDifficulty;

    Macronutrients macronutrients;

    int totalCalories;

    public MealDTO() {
    }

    public MealDTO(String name, List<ProductForMealDTO> products, int price,
                   MealDifficulty mealDifficulty, Macronutrients macronutrients, int totalCalories) {
        this.name = name;
        this.products = products;
        this.price = price;
        this.mealDifficulty = mealDifficulty;
        this.macronutrients = macronutrients;
        this.totalCalories = totalCalories;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductForMealDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductForMealDTO> products) {
        this.products = products;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public MealDifficulty getMealDifficulty() {
        return mealDifficulty;
    }

    public void setMealDifficulty(MealDifficulty mealDifficulty) {
        this.mealDifficulty = mealDifficulty;
    }

    public Macronutrients getMacronutrients() {
        return macronutrients;
    }

    public void setMacronutrients(Macronutrients macronutrients) {
        this.macronutrients = macronutrients;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }
}
