package codebuddies.MealooApp.dto;

import codebuddies.MealooApp.entities.meal.MealMacronutrients;

import java.time.LocalDate;
import java.util.List;

public class FoodDiaryDTO {

    Long id;

    LocalDate date;

    List<MealDTO> listOfMeals;

    MealMacronutrients mealMacronutrients;

    int totalCalories;

    double totalPrice;

    private FoodDiaryDTO() {
    }

    public FoodDiaryDTO(Long id, LocalDate date, List<MealDTO> listOfMeals
            , MealMacronutrients mealMacronutrients, int totalCalories, double totalPrice) {
        this.id = id;
        this.date = date;
        this.listOfMeals = listOfMeals;
        this.mealMacronutrients = mealMacronutrients;
        this.totalCalories = totalCalories;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MealMacronutrients getMealMacronutrients() {
        return mealMacronutrients;
    }

    public void setMealMacronutrients(MealMacronutrients mealMacronutrients) {
        this.mealMacronutrients = mealMacronutrients;
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

    public void addMeal(MealDTO meal){
        listOfMeals.add(meal);
    }

    public void deleteMeal(MealDTO meal){
        MealDTO mealToDelete = listOfMeals.stream()
                .filter(x -> x.getName().equals(meal.getName())).findAny()
                .orElseThrow(() ->
                        new RuntimeException("This diary does not contain" + meal.getName()));
        listOfMeals.remove(mealToDelete);
    }

}
