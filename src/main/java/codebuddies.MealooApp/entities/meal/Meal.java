package codebuddies.MealooApp.entities.meal;

import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.user.FoodDiary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    private List<Ingredient> ingredients;

    private double price;

    @NotNull
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

    public Meal(String name, List<Ingredient> ingredients, MealDifficulty mealDifficulty) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = calculatePrice();
        this.mealDifficulty = mealDifficulty;
        mealMacronutrients = calculateMealMacronutrients();
        totalCalories = calculateCalories();
    }

    public Meal(String name, List<Ingredient> ingredients, MealDifficulty mealDifficulty, String recipe) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = calculatePrice();
        this.mealDifficulty = mealDifficulty;
        this.recipe = recipe;
        mealMacronutrients = calculateMealMacronutrients();
        totalCalories = calculateCalories();
    }

    //todo set some params protected or private

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double calculatePrice(){
        double totalPrice = 0;
        for(int i = 0; i < ingredients.size(); i++){
            totalPrice  += (ingredients.get(i).getProduct().getPrice() * ingredients.get(i).getAmount()/1000);
        }
        return totalPrice;
    }

    protected int calculateCarbohydrates(){
        int totalCarbohydrates = 0;
        for(int i = 0; i < ingredients.size() ; i++){
            totalCarbohydrates += ingredients.get(i).getProduct().getMacronutrients().getCarbohydratesPer100g()
                    * ingredients.get(i).getAmount()  / 100;
        }
        return totalCarbohydrates;
    }
    protected int calculareProteins(){
        int totalProteins = 0;
        for(int i = 0; i < ingredients.size(); i++){
            totalProteins += ingredients.get(i).getProduct().getMacronutrients().getProteinsPer100g()
                    * ingredients.get(i).getAmount() / 100;
        }
        return totalProteins;
    }
    protected int calculateFats(){
        int totalFats = 0;
        for(int i = 0; i < ingredients.size() ; i++){
            totalFats += ingredients.get(i).getProduct().getMacronutrients().getFatsPer100g()
                    * ingredients.get(i).getAmount() / 100;
        }
        return totalFats;
    }

    protected MealMacronutrients calculateMealMacronutrients(){
        return new MealMacronutrients(calculareProteins(), calculateCarbohydrates(), calculateFats());
    }

    public int calculateCalories() {
        return (calculateCarbohydrates() * 4) + (calculareProteins() * 4) + (calculateFats() * 9);

    }

    public void recalculateData(){
        setPrice(calculatePrice());
        setMealMacronutrients(calculateMealMacronutrients());
        setTotalCalories(calculateCalories());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Double.compare(meal.price, price) == 0 &&
                totalCalories == meal.totalCalories &&
                Objects.equals(id, meal.id) &&
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
        return Objects.hash(id, name, ingredients, price, mealDifficulty, recipe, mealMacronutrients, totalCalories, foodDiaries, images);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", price=" + price +
                ", mealDifficulty=" + mealDifficulty +
                ", recipe='" + recipe + '\'' +
                ", mealMacronutrients=" + mealMacronutrients +
                ", totalCalories=" + totalCalories +
                ", foodDiaries=" + foodDiaries +
                ", images=" + images +
                '}';
    }
}
