package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Macronutrients;

import java.time.LocalDate;
import java.util.List;

public class FoodDiaryDTO {

    LocalDate date;

    List<MealDTO> listOfMeals;

    Macronutrients macronutrients;

    int totalCalories;

    double totalPrice;

    public FoodDiaryDTO() {
    }

    public FoodDiaryDTO(LocalDate date, List<MealDTO> listOfMeals
            , Macronutrients macronutrients, int totalCalories, double totalPrice) {
        this.date = date;
        this.listOfMeals = listOfMeals;
        this.macronutrients = macronutrients;
        this.totalCalories = totalCalories;
        this.totalPrice = totalPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<MealDTO> getListOfMeals() {
        return listOfMeals;
    }

    public void setListOfMeals(List<MealDTO> listOfMeals) {
        this.listOfMeals = listOfMeals;
    }

    public Macronutrients getMacronutrients() {
        return macronutrients;
    }

    public void setMacronutrients(Macronutrients macronutrients) {
        this.macronutrients = macronutrients;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
