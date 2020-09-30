package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.entities.user.WeightGoal;
import codebuddies.MealooApp.services.FoodDiaryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final FoodDiaryService diaryService;

    public ClientController(FoodDiaryService diaryService) {
        this.diaryService = diaryService; }


    @GetMapping(path = "/{id}/diaries")
    public ResponseEntity<Page<FoodDiaryDTO>> getAllDiaries(@PathVariable int id, Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(diaryService.getAllDiaries(id, pageable));
    }

    @GetMapping("/{id}/current")
    public ResponseEntity<FoodDiaryDTO> getCurrentDiary(@PathVariable int id) {
        return ResponseEntity.ok(diaryService.getCurrentDiary(id));
    }

    @GetMapping("/{id}/diary")
    public ResponseEntity<FoodDiaryDTO> getDiaryOfChosenDay(@PathVariable int id, @RequestParam("date") String date){
        return ResponseEntity.ok(diaryService.getDiaryByDate(id, date));
    }

    @PostMapping("/{id}/diary")
    public ResponseEntity<FoodDiaryDTO> createDiary(@PathVariable int id) {
        return ResponseEntity.ok(diaryService.createDiary(id));
    }

    @PostMapping("/{id}/add-meal")
    public ResponseEntity<FoodDiaryDTO> addMeal(@PathVariable int id, @RequestParam("mealName") String mealName) {
        return ResponseEntity.ok(diaryService.addMeal(id, mealName));
    }

    @DeleteMapping("/{id}/delete-meal")
    public ResponseEntity<FoodDiaryDTO> deleteMeal(@PathVariable int id, @RequestParam("mealName") String mealName) {
        return ResponseEntity.ok(diaryService.deleteMeal(id, mealName));
    }

    @GetMapping("/{id}/generate-customized-diary")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsAutomatically(@PathVariable int id,
                    @RequestParam("numberOfMeals") int numberOfMeals, @RequestParam("totalCalories") int totalCalories) {
        return ResponseEntity.ok(diaryService.generateDiet(numberOfMeals, totalCalories, id));
    }

    @GetMapping("/{id}/generate-diary")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsAccordingToWeightGoal(
                    @PathVariable int id, @RequestParam("numberOfMeals") int numberOfMeals
                    , @RequestParam int totalCalories, @RequestParam("weightGoal") WeightGoal weightGoal){
        return ResponseEntity.ok(diaryService.generateDiet(numberOfMeals, totalCalories, weightGoal, id));
    }
}
