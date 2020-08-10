package codebuddies.MealooApp.entities.user;

import javax.persistence.*;

@Entity
public class FakeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    private NutritionSettings nutritionSettings;

    @OneToOne
    private FoodDiary foodDiary;

    @Embedded
    private FakeUserDetails fakeUserDetails = new FakeUserDetails();


}
