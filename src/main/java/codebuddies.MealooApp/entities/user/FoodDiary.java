package codebuddies.MealooApp.entities.user;

import codebuddies.MealooApp.entities.meal.Meal;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "food_diary")
public class FoodDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "food_dairy")
    private List<Meal> listOfMeals;

    private LocalDate date;

    @OneToOne(mappedBy = "food_dairy")
    private FakeUser fakeUser;
}
