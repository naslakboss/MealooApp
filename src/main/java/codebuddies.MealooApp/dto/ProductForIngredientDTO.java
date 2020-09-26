package codebuddies.MealooApp.dto;

public class ProductForIngredientDTO {

    String name;

    double price;

    int caloriesPer100g;

    public ProductForIngredientDTO() {
    }

    public ProductForIngredientDTO(String name, double price, int caloriesPer100g) {
        this.name = name;
        this.price = price;
        this.caloriesPer100g = caloriesPer100g;

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


}
