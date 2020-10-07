package codebuddies.MealooApp.dto;

import codebuddies.MealooApp.entities.user.MealooUserDetails;
import codebuddies.MealooApp.entities.user.NutritionSettings;
import codebuddies.MealooApp.entities.user.Role;

import java.util.List;

public class MealooUserDTO {

    private Long id;

    private String username;

    private String password;

    private List<Role> roles;

    private String email;

    private NutritionSettings nutritionSettings;

    private MealooUserDetails mealooUserDetails;

    private List<FoodDiaryDTO> foodDiaries;

    public MealooUserDTO() {
    }

    public MealooUserDTO(Long id, String username, String password, List<Role> roles, String email,
                         NutritionSettings nutritionSettings, MealooUserDetails mealooUserDetails, List<FoodDiaryDTO> foodDiaries) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.nutritionSettings = nutritionSettings;
        this.mealooUserDetails = mealooUserDetails;
        this.foodDiaries = foodDiaries;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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

    public MealooUserDetails getMealooUserDetails() {
        return mealooUserDetails;
    }

    public void setMealooUserDetails(MealooUserDetails mealooUserDetails) {
        this.mealooUserDetails = mealooUserDetails;
    }

    public List<FoodDiaryDTO> getFoodDiaries() {
        return foodDiaries;
    }

    public void setFoodDiaries(List<FoodDiaryDTO> foodDiaries) {
        this.foodDiaries = foodDiaries;
    }
}
