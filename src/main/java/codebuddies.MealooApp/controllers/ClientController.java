package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.dataproviders.FoodDiaryProvider;
import codebuddies.MealooApp.entities.user.WeightGoal;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.FoodDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/client")
//@PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
public class ClientController {

    private FoodDiaryService diaryService;


    private FoodDiaryProvider foodDiaryProvider;


    @Autowired
    public ClientController(FoodDiaryService diaryService, FoodDiaryProvider foodDiaryProvider) {
        this.diaryService = diaryService;
        this.foodDiaryProvider = foodDiaryProvider;
    }

    @GetMapping("/{username}/diaries")
    public ResponseEntity<List<FoodDiaryDTO>> findAllDiaries(@PathVariable String username, Pageable pageable) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryProvider.findAllDiaries(username, pageable));
    }

    @GetMapping("/{username}/current")
    public ResponseEntity<FoodDiaryDTO> getCurrentDiary(@PathVariable String username) throws ResourceNotFoundException {
        return  ResponseEntity.ok(foodDiaryProvider.findTodaysDiary(username));
    }

    @GetMapping("/{username}/diary")
    public ResponseEntity<FoodDiaryDTO> getDiaryOfChosenDay(@PathVariable String username, @RequestParam("date") String date) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryProvider.findDiaryOfDay(username, date));
    }

    @PostMapping("/{username}/diary")
    public ResponseEntity<FoodDiaryDTO> createNewDiary(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryProvider.createNewDiary(username));
    }

    @PostMapping("/{username}/add-meal")
    public ResponseEntity<FoodDiaryDTO> addMealToCurrentDiary(@PathVariable String username, @RequestParam("mealName") String mealName) throws ResourceNotFoundException {
        diaryService.addMealToCurrentDiary(username, mealName);
        return ResponseEntity.ok(foodDiaryProvider.findTodaysDiary(username));
    }

    @DeleteMapping("/{username}/delete-meal")
    public ResponseEntity<FoodDiaryDTO> deleteMealFromCurrentDiary(@PathVariable String username, @RequestParam("mealName") String mealName) throws ResourceNotFoundException {
        diaryService.deleteMealFromCurrentDiary(username, mealName);
        return  ResponseEntity.ok(foodDiaryProvider.findTodaysDiary(username));
    }

    @GetMapping("/{username}/generate-customized-diary")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsAutomatically(@PathVariable String username
                    , @RequestParam("totalCalories") int totalCalories, @RequestParam("numberOfMeals") int numbersOfMeals){
        diaryService.generateDiet(totalCalories, numbersOfMeals, username);
        return  ResponseEntity.ok(foodDiaryProvider.findTodaysDiary(username));
    }

    @GetMapping("/{username}/generate-diary")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsToLossAndTakeCaloriesFromNutritionSettings(@PathVariable String username
                    , @RequestParam("numberOfMeals") int numbersOfMeals, @RequestParam("weightGoal") WeightGoal weightGoal ){
        diaryService.generateDiet(numbersOfMeals, weightGoal, username);
        return ResponseEntity.ok(foodDiaryProvider.findTodaysDiary(username));
    }
}
