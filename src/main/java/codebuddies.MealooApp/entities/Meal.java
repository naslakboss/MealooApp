package codebuddies.MealooApp.entities;




import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    public Object showMealDetailsByName(Meal searchedMeal) {
       return searchedMeal.getProducts().stream().map(product -> product.getMacronutrients())
                .mapToInt(macronutrients -> macronutrients.getFatPer100g()).sum();
    }
}
