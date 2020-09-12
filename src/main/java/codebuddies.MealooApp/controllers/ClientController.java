package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.FoodDiaryDTO;
import codebuddies.MealooApp.dataProviders.FoodDiaryFacade;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.FoodDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/client")
//@PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
public class ClientController {

    private FoodDiaryService diaryService;


    private FoodDiaryFacade foodDiaryFacade;


    @Autowired
    public ClientController(FoodDiaryService diaryService, FoodDiaryFacade foodDiaryFacade) {
        this.diaryService = diaryService;
        this.foodDiaryFacade = foodDiaryFacade;
    }

    @GetMapping("/{username}/diaries")
    public ResponseEntity<List<FoodDiaryDTO>> findAllDiaries(@PathVariable String username, Pageable pageable) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.findAllDiaries(username, pageable));
    }

    @GetMapping("/{username}/current")
    public ResponseEntity<FoodDiaryDTO> getTodayDiary(@PathVariable String username) throws ResourceNotFoundException {
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }

    @GetMapping("/{username}/diary")
    public ResponseEntity<FoodDiaryDTO> getDiaryOfGivenDay(@PathVariable String username, @RequestParam("date") String date) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.findDiaryOfDay(username, date));
    }

    @PostMapping("/{username}/diary")
    public ResponseEntity<FoodDiaryDTO> createNewDiary(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.createNewDiary(username));
    }

    @PostMapping("/{username}/add-meal")
    public ResponseEntity<FoodDiaryDTO> addMealToDiary(@PathVariable String username, @RequestParam("mealName") String mealName) throws ResourceNotFoundException {
        diaryService.addMealToCurrentDiary(username, mealName);
        return ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }

    @DeleteMapping("/{username}/delete-meal")
    public ResponseEntity<FoodDiaryDTO> deleteMealFromDiary(@PathVariable String username, @RequestParam("mealName") String mealName) throws ResourceNotFoundException {
        diaryService.deleteMealFromCurrentDiary(username, mealName);
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }
    // todo add algorithm

    @GetMapping("/{username}/generate-diary")
    public ResponseEntity<FoodDiaryDTO> generateListOfMealsAutomatically(@PathVariable String username
                    , @RequestParam("totalCalories") int totalCalories, @RequestParam("numberOfMeals") int numbersOfMeals){
        diaryService.generateDiet(totalCalories, numbersOfMeals, username);
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }
}
