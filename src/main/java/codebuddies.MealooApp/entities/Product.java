package codebuddies.MealooApp.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;



@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    private String name;

    private int price;

    private int caloriesPer100g;

    @Embedded
    private Macronutrients macronutrients;

//    private ProductType productType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meal_id")
    private Meal mealtest;

    public Product() {
    }

    public Product(String name, int price, int caloriesPer100g, Macronutrients macronutrients) {
        this.name = name;
        this.price = price;
        this.caloriesPer100g = caloriesPer100g;
        this.macronutrients = macronutrients;
//        this.productType = productType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Meal getMealtest() {
        return mealtest;
    }

    public void setMealtest(Meal mealtest) {
        this.mealtest = mealtest;
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

//    public ProductType getProductType() {
//        return productType;
//    }
//
//    public void setProductType(ProductType productType) {
//        this.productType = productType;
//    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", caloriesPer100g=" + caloriesPer100g +
                ", macronutrients=" + macronutrients +
//                ", productType=" + productType +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Product product = (Product) o;
//        return price == product.price &&
//                caloriesPer100g == product.caloriesPer100g &&
//                Objects.equals(name, product.name) &&
//                Objects.equals(macronutrients, product.macronutrients) //&&
//                Objects.equals(productType, product.productType);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, caloriesPer100g, macronutrients );
    }

}
