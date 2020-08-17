package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.FoodDiaryDTO;
import codebuddies.MealooApp.dataProviders.FoodDiaryFacade;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.services.FakeUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import codebuddies.MealooApp.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    FakeUserService userService;

    @Autowired
    FoodDiaryService diaryService;

    @Autowired
    MealService mealService;

    @Autowired
    FoodDiaryFacade foodDiaryFacade;


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
    public FoodDiaryDTO getTodayDiary(@PathVariable String username) {
        return  foodDiaryFacade.findTodayDiary(username);
    }

    @GetMapping("/getDiaryOfGivenDay/{username}/{date}")
    public FoodDiaryDTO getDiaryOfGivenDay(@PathVariable String username, @PathVariable String date){
        return foodDiaryFacade.findDiaryOfGivenDay(username, date);
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
