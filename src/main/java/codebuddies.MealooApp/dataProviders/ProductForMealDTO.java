package codebuddies.MealooApp.dataProviders;

public class ProductForMealDTO {

    String name;

    int price;

    int caloriesPer100g;


    public ProductForMealDTO() {
    }

    public ProductForMealDTO(String name, int price, int caloriesPer100g) {
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


}
