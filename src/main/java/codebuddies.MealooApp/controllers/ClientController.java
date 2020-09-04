package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.FoodDiaryDTO;
import codebuddies.MealooApp.dataProviders.FoodDiaryFacade;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.FoodDiaryService;
import codebuddies.MealooApp.services.MealService;
import codebuddies.MealooApp.services.MealooUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private FoodDiaryService diaryService;


    private FoodDiaryFacade foodDiaryFacade;


    @Autowired
    public ClientController(FoodDiaryService diaryService, FoodDiaryFacade foodDiaryFacade) {
        this.diaryService = diaryService;
        this.foodDiaryFacade = foodDiaryFacade;
    }

    @GetMapping("/diaries/{username}")
    public ResponseEntity<List<FoodDiaryDTO>> findAllDiaries(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.findAllDiaries(username));
    }

    @GetMapping("/todayDiary/{username}")
    public ResponseEntity<FoodDiaryDTO> getTodayDiary(@PathVariable String username) throws ResourceNotFoundException {
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }

    @GetMapping("/diary/{username}")
    public ResponseEntity<FoodDiaryDTO> getDiaryOfGivenDay(@PathVariable String username, @RequestParam("date") String date) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.findDiaryOfDay(username, date));
    }

    @PostMapping("/diary/{username}")
    public ResponseEntity<FoodDiaryDTO> createNewDiary(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.createNewDiary(username));
    }

    @PostMapping("/addMeal/{username}")
    public ResponseEntity<FoodDiaryDTO> addMealToDiary(@PathVariable String username, @RequestParam("mealName") String mealName) throws ResourceNotFoundException {
        diaryService.addMealToCurrentDiary(username, mealName);
        return ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }

    @DeleteMapping("/deleteMeal/{username}")
    public ResponseEntity<FoodDiaryDTO> deleteMealFromDiary(@PathVariable String username, @RequestParam("mealName") String mealName) throws ResourceNotFoundException {
        diaryService.deleteMealFromCurrentDiary(username, mealName);
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }
    // todo add algorithm

    @GetMapping("/generate/{username}")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsAutomatically(@PathVariable String username
                    , @RequestParam("totalCalories") int totalCalories, @RequestParam("numberOfMeals") int numbersOfMeals){
        diaryService.generateDiet(totalCalories, numbersOfMeals, username);
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }
}
