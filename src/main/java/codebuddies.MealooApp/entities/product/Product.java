package codebuddies.MealooApp.entities.product;

import codebuddies.MealooApp.entities.meal.Meal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;



@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int price;

    private int caloriesPer100g;

    @Embedded
    private Macronutrients macronutrients;

    private ProductType productType;

    @ManyToMany(mappedBy = "products")
    @JsonIgnoreProperties("products")
    private List<Meal> meals;


    public Product() {
    }

    public Product(String name, int price, int caloriesPer100g, Macronutrients macronutrients, ProductType productType) {
        this.name = name;
        this.price = price;
        this.macronutrients = macronutrients;
        this.caloriesPer100g = calculateCaloriesPer100g(this.caloriesPer100g);
        this.productType = productType;
    }

    public int calculateCaloriesPer100g(int caloriesPer100g){
        if(caloriesPer100g == 0) {
            return (macronutrients.getCarbohydratesPer100g() * 4) +
                    (macronutrients.getFatPer100g() * 9) + (macronutrients.getProteinPer100g() * 4);
        }
        return caloriesPer100g;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public void setCaloriesPer100g(int caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
    }

    public Macronutrients getMacronutrients() {
        return macronutrients;
    }

    public void setMacronutrients(Macronutrients macronutrients) {
        this.macronutrients = macronutrients;
    }


    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price &&
                caloriesPer100g == product.caloriesPer100g &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(macronutrients, product.macronutrients) &&
                productType == product.productType &&
                Objects.equals(meals, product.meals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, caloriesPer100g, macronutrients, productType, meals);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", caloriesPer100g=" + caloriesPer100g +
                ", macronutrients=" + macronutrients +
                ", productType=" + productType +
                ", meals=" + meals +
                '}';
    }
}
