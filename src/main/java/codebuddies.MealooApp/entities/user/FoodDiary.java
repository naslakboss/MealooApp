package codebuddies.MealooApp.entities.user;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class FoodDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JsonIgnoreProperties("foodDiaries")
    private List<Meal> listOfMeals;

    public LocalDate date;

    @ManyToOne
    @JsonIgnore
    private FakeUser fakeUser;

    private Macronutrients macronutrients;

    private int totalCalories;

    private int totalPrice;

    public FoodDiary() {
    }

    public FoodDiary (List<Meal> listOfMeals, LocalDate date, FakeUser fakeUser) {
        this.listOfMeals = listOfMeals;
        this.date = date;
        this.fakeUser = fakeUser;
        macronutrients = calculateMacronutrients();
        totalCalories = calculateCalories();
        totalPrice = calculatePrice();
    }

    public void addMeal(Meal meal){
        listOfMeals.add(meal);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public FakeUser getFakeUser() {
        return fakeUser;
    }

    public void setFakeUser(FakeUser fakeUsers) {
        this.fakeUser = fakeUser;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int calculatePrice() {
        return listOfMeals.stream().mapToInt(Meal::getPrice).sum();
    }

    public int calculateCalories(){
        int totalCalories = listOfMeals.stream().mapToInt(Meal::getTotalCalories).sum();
        return totalCalories;
     }

    public Macronutrients calculateMacronutrients(){
        Macronutrients macronutrients = new Macronutrients();
        int totalCarbohydrates = listOfMeals.stream().map(Meal::getMacronutrients)
                .mapToInt(Macronutrients::getCarbohydratesPer100g).sum();
        int totalProteins = listOfMeals.stream().map(Meal::getMacronutrients)
                .mapToInt(Macronutrients::getProteinPer100g).sum();
        int totalFats = listOfMeals.stream().map(Meal::getMacronutrients)
                .mapToInt(Macronutrients::getFatPer100g).sum();
        macronutrients.setCarbohydratesPer100g(totalCarbohydrates);
        macronutrients.setProteinPer100g(totalProteins);
        macronutrients.setFatPer100g(totalFats);
        return macronutrients;
    }

}
