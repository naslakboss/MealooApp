package codebuddies.MealooApp.entities.product;

import codebuddies.MealooApp.entities.meal.Meal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Range(min = 1)
    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
