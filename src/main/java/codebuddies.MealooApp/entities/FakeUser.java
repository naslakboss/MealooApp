package codebuddies.MealooApp.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class FakeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nick;

    private List<Meal> meals;

    private MealSettings mealSettings;

    @Embedded
    private FakeUserDetails fakeUserDetails = new FakeUserDetails();

}
