package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class MealooUserService {

    private MealooUserRepository mealooUserRepository;

    @Autowired
    public MealooUserService(MealooUserRepository mealooUserRepository) {
        this.mealooUserRepository = mealooUserRepository;
    }

    public List<MealooUser> findAll() {
        return mealooUserRepository.findAll();
    }

    public MealooUser save(MealooUser user) {
        return mealooUserRepository.save(user);
    }

    public MealooUser findByUsername(String username) throws ResourceNotFoundException {
        MealooUser user = mealooUserRepository.findByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException(username);
        }
        return user;
    }

    public MealooUser updateByUsername(String username, MealooUser mealooUser) {
        MealooUser patchedUser = mealooUserRepository.findByUsername(username);
        if(mealooUser.getPassword()!= null) {
            patchedUser.setPassword(mealooUser.getPassword());
        }
        if(mealooUser.getMealooUserDetails().getAge() != 0){
            patchedUser.getMealooUserDetails().setAge(mealooUser.getMealooUserDetails().getAge());
        }
        if(mealooUser.getMealooUserDetails().getHeight() != 0){
            patchedUser.getMealooUserDetails().setHeight(mealooUser.getMealooUserDetails().getHeight());
        }
        if(mealooUser.getMealooUserDetails().getWeight() != 0){
            patchedUser.getMealooUserDetails().setWeight(mealooUser.getMealooUserDetails().getWeight());
        }
        if(mealooUser.getMealooUserDetails().getPhysicalActivity() != null){
            patchedUser.getMealooUserDetails().setPhysicalActivity(mealooUser.getMealooUserDetails().getPhysicalActivity());
        }
        if(mealooUser.getMealooUserDetails().getSex() != null){
            patchedUser.getMealooUserDetails().setSex(mealooUser.getMealooUserDetails().getSex());
        }
        save(patchedUser);
        return patchedUser;
    }


    public Map calculateBMIAndCaloricDemand(MealooUser user) {
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
