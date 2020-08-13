package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.repositories.FakeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FakeUserService {

    @Autowired
    FakeUserRepository fakeUserRepository;
    // Wstrzykiwanie przez konstruktor

    @Autowired
    MealService mealService;

    public List<FakeUser> findAll() {
        return fakeUserRepository.findAll();
    }

    public FakeUser save(FakeUser user1) {
        return fakeUserRepository.save(user1);
    }

    public FakeUser findByUsername(String username) {
        return fakeUserRepository.findByUsername(username);
    }

    public Object getMealsAndRecipes() {

        List<Meal> meals = mealService.findAll();
        Map<String, Object> mealsList = new LinkedHashMap<>();
        int totalCalories = 0;
        int totalProteins = 0;
        int totalCarbohydrates = 0;
        int totalFats = 0;

        Meal meal1 = meals.get(0);
        mealsList.put("First Meal:" , meal1);
        mealsList.put("Recipe for meal 1:", "Smash beef and eat raw");
        Map<String, Integer> meal1Details = meal1.showMealDetailsByName(meal1);
        mealsList.put("Details of first meal :", meal1Details);
        totalCalories += meal1.showMealDetailsByName(meal1).get("Calories");
        totalProteins += meal1.showMealDetailsByName(meal1).get("Proteins");
        totalCarbohydrates += meal1.showMealDetailsByName(meal1).get("Carbohydrates");
        totalFats += meal1.showMealDetailsByName(meal1).get("Fats");


        Meal meal2 = meals.get(1);
        mealsList.put("Second Meal:" , meal2);
        mealsList.put("Recipe for meal 2:", "Example desc 1");
        Map<String, Integer> meal2Details = meal2.showMealDetailsByName(meal2);
        mealsList.put("Details of second meal:", meal2Details);
        totalCalories += meal2.showMealDetailsByName(meal2).get("Calories");
        totalProteins += meal2.showMealDetailsByName(meal2).get("Proteins");
        totalCarbohydrates += meal2.showMealDetailsByName(meal2).get("Carbohydrates");
        totalFats += meal2.showMealDetailsByName(meal2).get("Fats");

        Meal meal3 = meals.get(2);
        mealsList.put("Third Meal:" , meal3);
        mealsList.put("Recipe for meal 3:", "Example desc 2");
        Map<String, Integer> meal3Details = meal3.showMealDetailsByName(meal3);
        mealsList.put("Details of third :", meal3Details);
        totalCalories += meal3.showMealDetailsByName(meal3).get("Calories");
        totalProteins += meal3.showMealDetailsByName(meal3).get("Proteins");
        totalCarbohydrates += meal3.showMealDetailsByName(meal3).get("Carbohydrates");
        totalFats += meal3.showMealDetailsByName(meal3).get("Fats");

        Meal meal4 = meals.get(3);
        mealsList.put("Fourth Meal:" , meal4);
        mealsList.put("Recipe for meal 4:", "It's complicated algorithm");
        Map<String, Integer> meal4Details = meal4.showMealDetailsByName(meal4);
        mealsList.put("Details of fourth:", meal4Details);
        totalCalories += meal4.showMealDetailsByName(meal4).get("Calories");
        totalProteins += meal4.showMealDetailsByName(meal4).get("Proteins");
        totalCarbohydrates += meal4.showMealDetailsByName(meal4).get("Carbohydrates");
        totalFats += meal4.showMealDetailsByName(meal4).get("Fats");

        Meal meal5 = meals.get(4);
        mealsList.put("Fifth Meal:" , meal5);
        mealsList.put("Recipe for meal 5:", "And takes a lot of time to render data xD");
        Map<String, Integer> meal5Details = meal5.showMealDetailsByName(meal5);
        mealsList.put("Details of fifth:", meal5Details);
        totalCalories += meal5.showMealDetailsByName(meal5).get("Calories");
        totalProteins += meal5.showMealDetailsByName(meal5).get("Proteins");
        totalCarbohydrates += meal5.showMealDetailsByName(meal5).get("Carbohydrates");
        totalFats += meal5.showMealDetailsByName(meal5).get("Fats");
        mealsList.put("Total Daily Calories :", totalCalories);
        mealsList.put("Total Daily Proteins :", totalProteins);
        mealsList.put("Total Daily Carbohydrates :", totalCarbohydrates);
        mealsList.put("Total Daily Fats :", totalFats);
        // need to refractor this


        return mealsList;

    }
}
