package codebuddies.MealooApp.entities.user;

import codebuddies.MealooApp.entities.meal.Meal;
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

    public FoodDiary() {
    }

    public FoodDiary (List<Meal> listOfMeals, LocalDate date, FakeUser fakeUser) {
        this.listOfMeals = listOfMeals;
        this.date = date;
        this.fakeUser = fakeUser;
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
}
