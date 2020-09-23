package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.MealooUserProvider;
import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MealooUserService {

    private MealooUserProvider userProvider;


    public MealooUserService(MealooUserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public Page<MealooUser> getAllUsers(Pageable pageable) {
        return userProvider.getAllUsers(pageable);
    }

    public MealooUser getUserByUsername(String username) {
        return userProvider.getUserByUsername(username);
    }

    public MealooUser createUser(MealooUser user){
        return userProvider.createUser(user);
    }

    public MealooUser updateUserByUsername(MealooUser user, String username){
        MealooUser updatedUser = getUserByUsername(username);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setMealooUserRole(user.getMealooUserRole());
        updatedUser.setNutritionSettings(user.getNutritionSettings());
        updatedUser.setMealooUserDetails(user.getMealooUserDetails());
        return userProvider.updateUser(updatedUser);
    }

    @Transactional
    public void deleteByUsername(String username) {
        if(!existsByName(username)){
            throw new ResourceNotFoundException("User " + username + " does not exist in database");
        }
        else userProvider.deleteUserByUsername(username);
    }

    public boolean existsByName(String username){
        return userProvider.existsByUsername(username);
    }



    public Map calculateBMIAndCaloricDemand(String username) {
        MealooUser user = getUserByUsername(username);
        Map<String, Double> result = new LinkedHashMap<>();
        double userBMI = user.getMealooUserDetails()
                .calculateBMI();
        result.put("Your BMI is : ", userBMI);

        double caloricDemand = user.getMealooUserDetails().calculateCaloricDemand();

        result.put("Correct BMI for untrained people is from ", 18.5);
        result.put("to ", 25.0);
        result.put("Your caloric demand is :", caloricDemand);
        result.put("If you want to gain about 0.5kg per week, you should eat about", caloricDemand + 500);
        result.put("If you want to lose about 0.5kg per week, you should eat about", caloricDemand - 500);
        return result;
    }

}
