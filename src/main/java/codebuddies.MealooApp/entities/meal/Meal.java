package codebuddies.MealooApp.entities.meal;

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


    @Embedded
    private MealMacronutrients mealMacronutrients;

    private int totalCalories;

    @ManyToMany(mappedBy = "listOfMeals")
    @JsonIgnore
    private List<FoodDiary> foodDiaries;

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

    public double calculatePrice(){
        double totalPrice = 0;
        for(int i = 0; i < ingredients.size(); i++){
            totalPrice  += (ingredients.get(i).getProduct().getPrice() * ingredients.get(i).getAmount()/1000);
        }
        return totalPrice;
    }

    private int calculateCarbohydrates(){
        int totalCarbohydrates = 0;
        for(int i = 0; i < ingredients.size() ; i++){
            totalCarbohydrates += ingredients.get(i).getProduct().getMacronutrients().getCarbohydratesPer100g()
                    * ingredients.get(i).getAmount()  / 100;
        }
        return totalCarbohydrates;
    }
    private int calculareProteins(){
        int totalProteins = 0;
        for(int i = 0; i < ingredients.size(); i++){
            totalProteins += ingredients.get(i).getProduct().getMacronutrients().getProteinsPer100g()
                    * ingredients.get(i).getAmount() / 100;
        }
        return totalProteins;
    }
    private int calculateFats(){
        int totalFats = 0;
        for(int i = 0; i < ingredients.size() ; i++){
            totalFats += ingredients.get(i).getProduct().getMacronutrients().getFatsPer100g()
                    * ingredients.get(i).getAmount() / 100;
        }
        return totalFats;
    }

    private MealMacronutrients calculateMealMacronutrients(){
        return new MealMacronutrients(calculareProteins(), calculateCarbohydrates(), calculateFats());
    }

    public int calculateCalories() {
        return (calculateCarbohydrates() * 4) + (calculareProteins() * 4) + (calculateFats() * 9);

    }

    public void recalulateData(){
        calculatePrice();
        calculateMealMacronutrients();
        calculateCalories();
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
                Objects.equals(mealMacronutrients, meal.mealMacronutrients) &&
                Objects.equals(foodDiaries, meal.foodDiaries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ingredients, price, mealDifficulty, mealMacronutrients, totalCalories, foodDiaries);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", price=" + price +
                ", mealDifficulty=" + mealDifficulty +
                ", mealMacronutrients=" + mealMacronutrients +
                ", totalCalories=" + totalCalories +
                ", foodDiaries=" + foodDiaries +
                '}';
    }
}
