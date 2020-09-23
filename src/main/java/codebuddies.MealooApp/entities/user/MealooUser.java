package codebuddies.MealooApp.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class MealooUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Length(min = 5, max = 30)
    private String username;

    @NotBlank
    @Length(min = 6, max = 30)
    private String password;


    private MealooUserRole mealooUserRole;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Embedded
    private NutritionSettings nutritionSettings;

    @OneToMany(mappedBy = "mealooUser", orphanRemoval = true)
    @JsonIgnore
    private List<FoodDiary> foodDiaries;

    @Embedded
    private MealooUserDetails mealooUserDetails;

    public MealooUser() {
    }

    public MealooUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mealooUserRole = MealooUserRole.USER;
        this.nutritionSettings = new NutritionSettings(0);
        this.mealooUserDetails = new MealooUserDetails(0,0,0, Sex.MALE, PhysicalActivity.LITTLE);
    }

    public MealooUser(Long id, String username, String password, String email,
                      NutritionSettings nutritionSettings,
                      MealooUserDetails mealooUserDetails) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mealooUserRole = MealooUserRole.USER;
        this.nutritionSettings = nutritionSettings;
        this.mealooUserDetails = mealooUserDetails;
    }
    // todo add this functionality
//    public void setDailyCaloricGoalFromCalculator(){
//        nutritionSettings.setDailyCaloricGoal(mealooUserDetails.calculateCaloricDemand());
//    }
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

    public List<FoodDiary> getFoodDiaries() {
        return foodDiaries;
    }

    public void setFoodDiaries(List<FoodDiary> foodDiaries) {
        this.foodDiaries = foodDiaries;
    }

    void addDiary(FoodDiary foodDiary) {
        foodDiaries.add(foodDiary);
    }

    public MealooUserDetails getMealooUserDetails() {
        return mealooUserDetails;
    }

    public void setMealooUserDetails(MealooUserDetails mealooUserDetails) {
        this.mealooUserDetails = mealooUserDetails;
    }

    public MealooUserRole getMealooUserRole() {
        return mealooUserRole;
    }

    public void setMealooUserRole(MealooUserRole mealooUserRole) {
        this.mealooUserRole = mealooUserRole;
    }

}
