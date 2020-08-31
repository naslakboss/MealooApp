package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.FoodDiaryDTO;
import codebuddies.MealooApp.dataProviders.FoodDiaryFacade;
import codebuddies.MealooApp.entities.user.MealooUser;
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

    private MealService mealService;

    private FoodDiaryFacade foodDiaryFacade;

    private MealooUserService mealooUserService;

    @Autowired
    public ClientController(FoodDiaryService diaryService, MealService mealService, FoodDiaryFacade foodDiaryFacade,@Lazy MealooUserService mealooUserService) {
        this.diaryService = diaryService;
        this.mealService = mealService;
        this.foodDiaryFacade = foodDiaryFacade;
        this.mealooUserService = mealooUserService;
    }

    @GetMapping("/allDiaries/{username}")
    public ResponseEntity<List<FoodDiaryDTO>> findAllDiaries(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.findAllDiaries(username));
    }

    @GetMapping("/getTodaysDiary/{username}")
    public ResponseEntity<FoodDiaryDTO> getTodayDiary(@PathVariable String username) throws ResourceNotFoundException {
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }

    @GetMapping("/getDiaryOfDay/{username}/{date}")
    public ResponseEntity<FoodDiaryDTO> getDiaryOfGivenDay(@PathVariable String username, @PathVariable String date) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.findDiaryOfDay(username, date));
    }

    @PostMapping("/createNewDiary/{username}")
    public ResponseEntity<FoodDiaryDTO> createNewDiary(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(foodDiaryFacade.createNewDiary(username));
    }

    @PostMapping("/addMeal/{username}/{mealName}")
    public ResponseEntity<FoodDiaryDTO> addMealToDiary(@PathVariable String username, @PathVariable String mealName) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(username);
        diaryService.addMealToCurrentDiary(user, mealName);
        return ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }

    @DeleteMapping("/deleteMeal/{username}/{mealName}")
    public ResponseEntity<FoodDiaryDTO> deleteMealFromDiary(@PathVariable String username, @PathVariable String mealName) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(username);
        diaryService.deleteMealFromCurrentDiary(user, mealName);
        return  ResponseEntity.ok(foodDiaryFacade.findTodaysDiary(username));
    }
    // todo add algorithm
}
