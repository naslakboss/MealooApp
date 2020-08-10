package codebuddies.MealooApp.entities.meal;




import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.user.FoodDiary;
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

    @ManyToOne
    private FoodDiary foodDiary;

    public Meal() {
    }

    public Meal(Long id,String name, List<Product> products, int price, MealDifficulty mealDifficulty) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.price = price;
        this.mealDifficulty = mealDifficulty;
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
