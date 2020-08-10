package codebuddies.MealooApp.entities.user;

import codebuddies.MealooApp.entities.meal.Meal;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class FoodDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "foodDiary")
    private List<Meal> listOfMeals;

    private LocalDate date;

    @OneToOne(mappedBy = "foodDiary")
    private FakeUser fakeUser;

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

    public void setFakeUser(FakeUser fakeUser) {
        this.fakeUser = fakeUser;
    }
}
