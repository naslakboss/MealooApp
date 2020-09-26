package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.entities.user.WeightGoal;
import codebuddies.MealooApp.services.FoodDiaryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
//@PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
public class ClientController {

    private FoodDiaryService diaryService;

    public ClientController(FoodDiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping("/{username}/diaries")
    public ResponseEntity<Page<FoodDiaryDTO>> getAllDiaries(@PathVariable String username, Pageable pageable) {
        return ResponseEntity.ok(diaryService.getAllDiaries(username, pageable));
    }

    @GetMapping("/{username}/current")
    public ResponseEntity<FoodDiaryDTO> getCurrentDiary(@PathVariable String username) {
        return ResponseEntity.ok(diaryService.getCurrentDiary(username));
    }

    @GetMapping("/{username}/diary")
    public ResponseEntity<FoodDiaryDTO> getDiaryOfChosenDay(@PathVariable String username, @RequestParam("date") String date){
        return ResponseEntity.ok(diaryService.getDiaryByDate(username, date));
    }

    @PostMapping("/{username}/diary")
    public ResponseEntity<FoodDiaryDTO> createDiary(@PathVariable String username) {
        return ResponseEntity.ok(diaryService.createDiary(username));
    }

    @PostMapping("/{username}/add-meal")
    public ResponseEntity<FoodDiaryDTO> addMeal(@PathVariable String username, @RequestParam("mealName") String mealName) {
        return ResponseEntity.ok(diaryService.addMeal(username, mealName));
    }

    @DeleteMapping("/{username}/delete-meal")
    public ResponseEntity<FoodDiaryDTO> deleteMeal(@PathVariable String username, @RequestParam("mealName") String mealName) {
        return ResponseEntity.ok(diaryService.deleteMeal(username, mealName));
    }

    @GetMapping("/{username}/generate-customized-diary")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsAutomatically(@PathVariable String username
                    , @RequestParam("totalCalories") int totalCalories, @RequestParam("numberOfMeals") int numberOfMeals){
        return ResponseEntity.ok(diaryService.generateDiet(totalCalories, numberOfMeals, username));
    }

    @GetMapping("/{username}/generate-diary")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsTakingCaloriesFromNutritionSettings(@PathVariable String username
                    , @RequestParam("numberOfMeals") int numberOfMeals, @RequestParam("weightGoal") WeightGoal weightGoal ){
        return ResponseEntity.ok(diaryService.generateDiet(numberOfMeals, weightGoal, username));
    }
}
