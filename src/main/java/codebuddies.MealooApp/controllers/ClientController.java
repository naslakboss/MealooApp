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

import java.time.LocalDate;
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


    @GetMapping("/getAllDiaries/{username}")
    public List<FoodDiaryDTO> getAllDiariesForGivenUser(@PathVariable String username){
        return foodDiaryFacade.getAllDiariesForGivenUser(username);
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
    public FoodDiaryDTO createNewDiary(@PathVariable String username){
        return foodDiaryFacade.createNewFoodDiary(username);
    }

    @PostMapping("/addMeal/{username}/{name}")
    public FoodDiaryDTO addMealToDiary(@PathVariable String username, @PathVariable String name){
        FakeUser user = userService.findByUsername(username);
        Meal meal = mealService.findByName(name);
        diaryService.addFoodToTodayDiary(user, meal);
        String presentDate = LocalDate.now().toString();
        return foodDiaryFacade.findDiaryOfGivenDay(username, presentDate);
    }

    @DeleteMapping("/deleteMeal/{username}/{name}")
    public FoodDiaryDTO deleteMealFromDiary(@PathVariable String username, @PathVariable String name){
        FakeUser user = userService.findByUsername(username);
        Meal mealToDelete = mealService.findByName(name);
        diaryService.deleteMealFromTodayDiary(user, mealToDelete);
        String presentDate = LocalDate.now().toString();
        return  foodDiaryFacade.findDiaryOfGivenDay(username, presentDate);
    }


}
