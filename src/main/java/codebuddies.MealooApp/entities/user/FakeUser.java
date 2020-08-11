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

    @Embedded
    private NutritionSettings nutritionSettings;

    @OneToOne
    private FoodDiary foodDiary;

    @Embedded
    private FakeUserDetails fakeUserDetails = new FakeUserDetails();

    public FakeUser() {
    }

    // There sould be builder pattern but we switch to oauth2 later


    public FakeUser(Long id, String username, String password, String email, NutritionSettings nutritionSettings) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nutritionSettings = nutritionSettings;
    }

    public FakeUser(Long id, String username, String password, String email,
                    NutritionSettings nutritionSettings, FoodDiary foodDiary,
                    FakeUserDetails fakeUserDetails) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nutritionSettings = nutritionSettings;
        this.foodDiary = foodDiary;
        this.fakeUserDetails = fakeUserDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NutritionSettings getNutritionSettings() {
        return nutritionSettings;
    }

    public void setNutritionSettings(NutritionSettings nutritionSettings) {
        this.nutritionSettings = nutritionSettings;
    }

    public FoodDiary getFoodDiary() {
        return foodDiary;
    }

    public void setFoodDiary(FoodDiary foodDiary) {
        this.foodDiary = foodDiary;
    }

    public FakeUserDetails getFakeUserDetails() {
        return fakeUserDetails;
    }

    public void setFakeUserDetails(FakeUserDetails fakeUserDetails) {
        this.fakeUserDetails = fakeUserDetails;
    }
}
