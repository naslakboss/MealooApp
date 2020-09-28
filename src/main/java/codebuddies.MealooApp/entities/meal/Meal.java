package codebuddies.MealooApp.entities.meal;

import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.user.FoodDiary;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Meal {

    @Id
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    private double price;

    private MealDifficulty mealDifficulty;

    private String recipe;

    @Embedded
    private MealMacronutrients mealMacronutrients;

    private int totalCalories;

    @ManyToMany(mappedBy = "listOfMeals")
    @JsonIgnore
    private List<FoodDiary> foodDiaries;

    @OneToMany(mappedBy = "meal")
    private List<Image> images;

    public Meal() {
    }

    public Meal(String name, List<Ingredient> ingredients, double price,
                MealDifficulty mealDifficulty, String recipe,
                MealMacronutrients mealMacronutrients, int totalCalories, List<FoodDiary> foodDiaries, List<Image> images) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.mealDifficulty = mealDifficulty;
        this.recipe = recipe;
        this.mealMacronutrients = mealMacronutrients;
        this.totalCalories = totalCalories;
        this.foodDiaries = foodDiaries;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
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

    public List<FoodDiary> getFoodDiaries() {
        return foodDiaries;
    }

    public void setFoodDiaries(List<FoodDiary> foodDiaries) {
        this.foodDiaries = foodDiaries;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Double.compare(meal.price, price) == 0 &&
                totalCalories == meal.totalCalories &&
                Objects.equals(name, meal.name) &&
                Objects.equals(ingredients, meal.ingredients) &&
                mealDifficulty == meal.mealDifficulty &&
                Objects.equals(recipe, meal.recipe) &&
                Objects.equals(mealMacronutrients, meal.mealMacronutrients) &&
                Objects.equals(foodDiaries, meal.foodDiaries) &&
                Objects.equals(images, meal.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients, price, mealDifficulty, recipe, mealMacronutrients, totalCalories, foodDiaries, images);
    }
}
