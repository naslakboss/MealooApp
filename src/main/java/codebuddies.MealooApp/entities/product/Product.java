package codebuddies.MealooApp.entities.product;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
public class Product {

    @Id
    private String name;

    private double price;

    private int caloriesPer100g;

    @Embedded
    private Macronutrients macronutrients;

    private ProductType productType;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Ingredient> ingredients;

    public Product() {
    }

    public Product(String name, double price, Macronutrients macronutrients, int caloriesPer100g, ProductType productType) {
        this.name = name;
        this.price = price;
        this.macronutrients = macronutrients;
        this.caloriesPer100g = caloriesPer100g;
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price &&
                caloriesPer100g == product.caloriesPer100g &&
                Objects.equals(name, product.name) &&
                Objects.equals(macronutrients, product.macronutrients) &&
                productType == product.productType &&
                Objects.equals(ingredients, product.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, caloriesPer100g, macronutrients, productType, ingredients);
    }

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", caloriesPer100g=" + caloriesPer100g +
                ", macronutrients=" + macronutrients +
                ", productType=" + productType +
                ", ingredients=" + ingredients +
                '}';
    }
}
