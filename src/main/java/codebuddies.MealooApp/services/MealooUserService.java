package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.MealooUserMapper;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.user.MealooUserDetails;
import codebuddies.MealooApp.entities.user.Sex;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MealooUserService {

    private final MealooUserMapper userMapper;

    private final PasswordEncoder passwordEncoder;


    public MealooUserService(MealooUserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<MealooUserDTO> getAllUsers(Pageable pageable) {
        return userMapper.getAllUsers(pageable);
    }

    public boolean existsByName(String username) {
        return userMapper.existsByUsername(username);
    }

    public MealooUserDTO getUserById(int id){
        return userMapper.getUserById(id);
    }

    public MealooUserDTO getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public MealooUserDTO createUser(MealooUserDTO user) {
        if (existsByName(user.getUsername())) {
            throw new EntityAlreadyFoundException(user.getUsername());
        }
        passwordEncoder.encode(user.getPassword());
        return userMapper.createUser(user);
    }

    public MealooUserDTO updateUserByUsername(MealooUserDTO user, String username) {
        if (!existsByName(username)) {
            throw new ResourceNotFoundException(username);
        }

        MealooUserDTO oldData = getUserByUsername(username);
        user.setId(oldData.getId());
        user.setFoodDiaries(oldData.getFoodDiaries());

        return userMapper.updateUser(user);
    }

    @Transactional
    public void deleteByUsername(String username) {
        userMapper.deleteUserByUsername(username);
    }

    public double calculateBMI(MealooUserDTO mealooUserDTO){
        MealooUserDetails userDetails = mealooUserDTO.getMealooUserDetails();
        return (double) userDetails.getWeight() * 10000 / Math.pow(userDetails.getHeight(),2);
    }

    public int calculateCaloricDemand(MealooUserDTO mealooUserDTO){
        MealooUserDetails userDetails = mealooUserDTO.getMealooUserDetails();
        if(userDetails.getSex() == Sex.MALE){
            return (66 + (14 * userDetails.getWeight()) + (5 * userDetails.getHeight())
                    - (6 * userDetails.getAge())) * (userDetails.getPhysicalActivity().getMultiplier()/10);
        }
        return (655 + (10 * userDetails.getWeight()) + (2 * userDetails.getHeight())
                - (5 * userDetails.getAge())) * (userDetails.getPhysicalActivity().getMultiplier()/10);
    }
    public Map<String, Double> giveBMIAndCaloricDemandInformation(String username) {
        MealooUserDTO user = getUserByUsername(username);
        Map<String, Double> result = new LinkedHashMap<>();

        double userBMI = calculateBMI(user);
        result.put("Your BMI is : ", userBMI);
        result.put("Correct BMI for untrained people is from ", 18.5);
        result.put("to ", 25.0);

        double caloricDemand = calculateCaloricDemand(user);
        result.put("Your caloric demand is :", caloricDemand);
        result.put("To gain about 0.5kg per week, eat about", caloricDemand + 500);
        result.put("To lose about 0.5kg per week, eat about", caloricDemand - 500);

        return result;
    }

}
