package codebuddies.MealooApp.entities.meal;

import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.user.FoodDiary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
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

    private MealDifficulty mealDifficulty;

    private Macronutrients macronutrients;

    private int totalCalories;

    @ManyToMany(mappedBy = "listOfMeals")
    @JsonIgnore
    private List<FoodDiary> foodDiaries;

    public Meal() {
    }

    public Meal(long id, String name, List<Ingredient> ingredients, MealDifficulty mealDifficulty) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.price = calculatePrice();
        this.mealDifficulty = mealDifficulty;
        macronutrients = calculateMacronutrients();
        totalCalories = calculateCalories();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<FoodDiary> getFoodDiaries() {
        return foodDiaries;
    }

    public void setFoodDiaries(List<FoodDiary> foodDiaries) {
        this.foodDiaries = foodDiaries;
    }

    private double calculatePrice(){
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

    private Macronutrients calculateMacronutrients(){
        return new Macronutrients(calculareProteins(), calculateCarbohydrates(), calculateFats());
    }

    private int calculateCalories() {
        return (calculateCarbohydrates() * 4) + (calculareProteins() * 4) + (calculateFats() * 9);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return price == meal.price &&
                totalCalories == meal.totalCalories &&
                Objects.equals(id, meal.id) &&
                Objects.equals(name, meal.name) &&
                Objects.equals(ingredients, meal.ingredients) &&
                mealDifficulty == meal.mealDifficulty &&
                Objects.equals(macronutrients, meal.macronutrients) &&
                Objects.equals(foodDiaries, meal.foodDiaries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ingredients, price, mealDifficulty, macronutrients, totalCalories, foodDiaries);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", price=" + price +
                ", mealDifficulty=" + mealDifficulty +
                ", macronutrients=" + macronutrients +
                ", totalCalories=" + totalCalories +
                ", foodDiaries=" + foodDiaries +
                '}';
    }
}
