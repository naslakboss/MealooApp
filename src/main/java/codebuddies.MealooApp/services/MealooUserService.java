package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.MealooUserProvider;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
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

    public Page<MealooUserDTO> getAllUsers(Pageable pageable) {
        return userProvider.getAllUsers(pageable);
    }

    public MealooUserDTO getUserByUsername(String username) {
        if(!existsByName(username)){
            throw new ResourceNotFoundException(username);
        }
        return userProvider.getUserByUsername(username);
    }

    public MealooUserDTO createUser(MealooUserDTO user){
        if(existsByName(user.getUsername())){
            throw new EntityAlreadyFoundException(user.getUsername());
        }
        return userProvider.createUser(user);
    }

    public MealooUserDTO updateUserByUsername(MealooUserDTO user, String username){
        if(!existsByName(username)){
            throw new ResourceNotFoundException(username);
        }
        user.setId(getUserByUsername(username).getId());
        return userProvider.updateUser(user);
    }

    @Transactional
    public void deleteByUsername(String username) {
        if(!existsByName(username)){
            throw new ResourceNotFoundException(username);
        }
        else userProvider.deleteUserByUsername(username);
    }

    public boolean existsByName(String username){
        return userProvider.existsByUsername(username);
    }



    public Map calculateBMIAndCaloricDemand(String username) {
        MealooUserDTO user = getUserByUsername(username);
        Map<String, Double> result = new LinkedHashMap<>();
        double userBMI = user.getMealooUserDetails()
                .calculateBMI();
        result.put("Your BMI is : ", userBMI);

        double caloricDemand = user.getMealooUserDetails().calculateCaloricDemand();

        result.put("Correct BMI for untrained people is from ", 18.5);
        result.put("to ", 25.0);
        result.put("Your caloric demand is :", caloricDemand);
        result.put("To gain about 0.5kg per week, eat about", caloricDemand + 500);
        result.put("To lose about 0.5kg per week, eat about", caloricDemand - 500);
        return result;
    }

}
