package codebuddies.MealooApp.entities.meal;




import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.user.FoodDiary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Meal {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany
    @JsonIgnoreProperties("meals")
    private List<Product> products;

    private int price;

    private MealDifficulty mealDifficulty;

    private Macronutrients macronutrients;

    private int totalCalories;

    @ManyToMany(mappedBy = "listOfMeals")
    @JsonIgnore
    private List<FoodDiary> foodDiaries;

    public Meal() {
    }

    public Meal(Long id,String name, List<Product> products, int price, MealDifficulty mealDifficulty) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.price = price;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
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

    public List<FoodDiary> getFoodDiaries() {
        return foodDiaries;
    }

    public void setFoodDiaries(List<FoodDiary> foodDiaries) {
        this.foodDiaries = foodDiaries;
    }

    public Macronutrients calculateMacronutrients(){
        Macronutrients macronutrients = new Macronutrients();
        int totalCarbohydrates = products.stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getCarbohydratesPer100g).sum() / products.size();
        int totalProteins = products.stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getProteinPer100g).sum() / products.size();
        int totalFats = products.stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getFatPer100g).sum() / products.size();
        macronutrients.setCarbohydratesPer100g(totalCarbohydrates);
        macronutrients.setProteinPer100g(totalProteins);
        macronutrients.setFatPer100g(totalFats);
        return macronutrients;
    }

    private int calculateCalories() {
        int totalCarbohydratesCalories = products.stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getCarbohydratesPer100g).sum() * 4;
        int totalProteinsCalories = products.stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getProteinPer100g).sum() * 4;
        int totalFatsCalories = products.stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getFatPer100g).sum() * 9;
        int totalCalories = totalCarbohydratesCalories + totalProteinsCalories + totalFatsCalories;
        return totalCalories / products.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return price == meal.price &&
                Objects.equals(id, meal.id) &&
                Objects.equals(name, meal.name) &&
                Objects.equals(products, meal.products) &&
                mealDifficulty == meal.mealDifficulty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, products, price, mealDifficulty);
    }

    public Map<String, Integer> showMealDetailsByName(Meal searchedMeal) {

        int totalCalories = searchedMeal.getProducts().stream().
                mapToInt(Product::getCaloriesPer100g).sum();
        int totalProteins = searchedMeal.getProducts().stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getProteinPer100g).sum();
        int totalCarbohydrates = searchedMeal.getProducts().stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getCarbohydratesPer100g).sum();
        int totalFats = searchedMeal.getProducts().stream().map(Product::getMacronutrients)
                .mapToInt(Macronutrients::getFatPer100g).sum();
        Map<String, Integer> details = new HashMap<>();
        details.put("Calories", totalCalories);
        details.put("Proteins", totalProteins);
        details.put("Carbohydrates", totalCarbohydrates);
        details.put("Fats", totalFats);
        return details;
    }
}
