package codebuddies.MealooApp.entities.user;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.product.Macronutrients;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class FoodDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany
    @JsonIgnoreProperties("foodDiaries")
    private List<Meal> listOfMeals;

    public LocalDate date;

    @ManyToOne
    @JsonIgnore
    private MealooUser mealooUser;

    private MealMacronutrients mealMacronutrients;

    private int totalCalories;

    private float totalPrice;

    public FoodDiary() {
    }

    public FoodDiary (List<Meal> listOfMeals, LocalDate date, MealooUser mealooUser) {
        this.listOfMeals = listOfMeals;
        this.date = date;
        this.mealooUser = mealooUser;
        mealMacronutrients = calculateMealMacronutrients();
        totalCalories = calculateCalories();
        totalPrice = calculatePrice();
    }
    //todo set some params protected or private

    public void addMeal(Meal meal){
        listOfMeals.add(meal);
    }

    public void deleteMeal(Meal meal) {
        listOfMeals.remove(meal);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Meal> getListOfMeals() {
        return listOfMeals;
    }

    public void setListOfMeals(List<Meal> listOfMeals) {
        this.listOfMeals = listOfMeals;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MealooUser getFakeUser() {
        return mealooUser;
    }

    public void setFakeUser(MealooUser mealooUsers) {
        this.mealooUser = mealooUser;
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

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float calculatePrice() {
        return (float)listOfMeals.stream().mapToDouble(Meal::getPrice).sum();
    }

    public int calculateCalories(){
        return listOfMeals.stream().mapToInt(Meal::getTotalCalories).sum();
     }

    public MealMacronutrients calculateMealMacronutrients(){
        MealMacronutrients mealMacronutrients = new MealMacronutrients();
        int totalCarbohydrates = listOfMeals.stream().map(Meal::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalCarbohydrates).sum();
        int totalProteins = listOfMeals.stream().map(Meal::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalProteins).sum();
        int totalFats = listOfMeals.stream().map(Meal::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalFats).sum();
        mealMacronutrients.setTotalCarbohydrates(totalCarbohydrates);
        mealMacronutrients.setTotalProteins(totalProteins);
        mealMacronutrients.setTotalFats(totalFats);
        return mealMacronutrients;
    }

}
