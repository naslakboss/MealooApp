package codebuddies.MealooApp.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
public class MealooUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private MealooUserRole mealooUserRole;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Embedded
    private NutritionSettings nutritionSettings;

    @OneToMany(mappedBy = "mealooUser")
    @JsonIgnore
    private List<FoodDiary> foodDiaries;

    @Embedded
    private MealooUserDetails mealooUserDetails;

    public MealooUser() {
    }

    public MealooUser(String username, String password, String email, NutritionSettings nutritionSettings) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mealooUserRole = MealooUserRole.USER;
        this.nutritionSettings = nutritionSettings;
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

    public void addDiary(FoodDiary foodDiary) {
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
