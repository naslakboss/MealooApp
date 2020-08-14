package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.user.FoodDiary;

import java.util.List;

public class MealDTO {

    String name;

    List<ProductDTO> products;

    int price;

    MealDifficulty mealDifficulty;

    public MealDTO() {
    }

    public MealDTO(String name, List<ProductDTO> products, int price, MealDifficulty mealDifficulty) {
        this.name = name;
        this.products = products;
        this.price = price;
        this.mealDifficulty = mealDifficulty;;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
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

}
