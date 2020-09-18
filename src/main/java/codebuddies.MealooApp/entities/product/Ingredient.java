package codebuddies.MealooApp.entities.product;

import codebuddies.MealooApp.entities.meal.Meal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Min(value = 1, message = "Amount of product in meal cannot be less than 1g")
    private int amount;

    @ManyToOne
    private Product product;

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private List<Meal> meals;

    public Ingredient() {
    }

    public Ingredient(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
