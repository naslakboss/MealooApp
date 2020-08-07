package codebuddies.MealooApp.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;
import java.util.Objects;

@Document
public class Meal {

    @Id
    private String name;

    private List<Product> products;

    private int price;

    private String mealDifficulty;


    public Meal() {
    }

    public Meal(String name, List<Product> products, int price, String mealDifficulty) {
        this.name = name;
        this.products = products;
        this.price = price;
        this.mealDifficulty = mealDifficulty;
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

    public String getMealDifficulty() {
        return mealDifficulty;
    }

    public void setMealDifficulty(String mealDifficulty) {
        this.mealDifficulty = mealDifficulty;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", products=" + products +
                ", price=" + price +
                ", mealDifficulty=" + mealDifficulty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return price == meal.price &&
                Objects.equals(name, meal.name) &&
                Objects.equals(products, meal.products) &&
                Objects.equals(mealDifficulty, meal.mealDifficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, products, price, mealDifficulty);
    }
}
