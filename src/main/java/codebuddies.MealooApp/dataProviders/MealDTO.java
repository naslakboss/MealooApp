package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.product.Macronutrients;

import java.util.List;

public class MealDTO {

    String name;

    List<IngredientForMealDTO> ingredients;

    double price;

    MealDifficulty mealDifficulty;

    MealMacronutrients mealMacronutrients;

    int totalCalories;

    String recipe;

    public MealDTO() {
    }

    public MealDTO(String name, List<IngredientForMealDTO> ingredients, double price,
                   MealDifficulty mealDifficulty, MealMacronutrients mealMacronutrients, int totalCalories, String recipe) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.mealDifficulty = mealDifficulty;
        this.mealMacronutrients = mealMacronutrients;
        this.totalCalories = totalCalories;
        this.recipe = recipe;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientForMealDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientForMealDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MealDifficulty getMealDifficulty() {
        return mealDifficulty;
    }

    public void setMealDifficulty(MealDifficulty mealDifficulty) {
        this.mealDifficulty = mealDifficulty;
    }

    public MealMacronutrients getMealMacronutrients() {
        return mealMacronutrients;
    }

    public void setMealMacronutrients(MealMacronutrients mealMacronutrients) {
        this.mealMacronutrients = mealMacronutrients;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
