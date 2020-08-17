package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.repositories.MealRepository;
import codebuddies.MealooApp.services.FakeUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import codebuddies.MealooApp.services.MealService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    FakeUserService userService;

    @Autowired
    FoodDiaryService diaryService;

    @Autowired
    MealService mealService;


    @GetMapping("/testListOfMeals")
    public Object getMealsAndRecipesForWholeDay(){
        return userService.getMealsAndRecipes();
    }

    @GetMapping("/getAllDiaries/{username}")
    public List<FoodDiary> getAllDiariesForGivenUser(@PathVariable String username){
        FakeUser user = userService.findByUsername(username);
        return diaryService.findAllDiariesForUser(user);
    }

    @GetMapping("/getTodayDiary/{username}")
    public FoodDiary getTodayDiary(@PathVariable String username) {
        FakeUser user = userService.findByUsername(username);
        return diaryService.findTodayDiary(user);
    }

    @GetMapping("/getDiaryOfGivenDay/{username}/{date}")
    public FoodDiary getDiaryOfGivenDay(@PathVariable String username, @PathVariable String date){
        FakeUser user = userService.findByUsername(username);
        return diaryService.findDiaryOfGivenDate(user, date);
    }

    @PostMapping("/createNewFoodDiary/{username}")
    public FoodDiary createNewDiary(@PathVariable String username){
        FakeUser user = userService.findByUsername(username);
        return diaryService.createNewFoodDiary(user);
    }

    @PostMapping("/addMeal/{username}/{name}")
    public FoodDiary addMealToDiary(@PathVariable String username, @PathVariable String name){
        FakeUser user = userService.findByUsername(username);
        Meal meal = mealService.findByName(name);
        return diaryService.addFoodToTodayDiary(user, meal);
    }

    @DeleteMapping("/deleteMeal/{username}/{name}")
    public FoodDiary deleteMealFromDiary(@PathVariable String username, @PathVariable String name){
        FakeUser user = userService.findByUsername(username);
        Meal mealToDelete = mealService.findByName(name);
        return diaryService.deleteMealFromTodayDiary(user, mealToDelete);
    }


}
