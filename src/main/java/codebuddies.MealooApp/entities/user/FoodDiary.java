package codebuddies.MealooApp.entities.user;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FoodDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany
    private List<Meal> listOfMeals;

    public LocalDate date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private MealooUser mealooUser;

    private MealMacronutrients mealMacronutrients;

    private int totalCalories;

    private float totalPrice;

    public FoodDiary() {
    }

    public FoodDiary (List<Meal> listOfMeals, LocalDate date, MealooUser mealooUser) {
        this.listOfMeals = new ArrayList<>();
        this.date = date;
        this.mealooUser = mealooUser;
        mealMacronutrients = new MealMacronutrients(0,0,0);
        totalCalories = 0;
        totalPrice = 0;
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

    public MealooUser getMealooUser() {
        return mealooUser;
    }

    public void setMealooUser(MealooUser mealooUser) {
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




}
