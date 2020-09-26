package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.FoodDiaryProvider;
import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.user.WeightGoal;
import codebuddies.MealooApp.exceptions.RequiredMealsNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodDiaryService {


    private FoodDiaryProvider diaryProvider;

    private MealooUserService userService;

    private MealService mealService;

    public FoodDiaryService(FoodDiaryProvider diaryProvider, MealooUserService userService, MealService mealService) {
        this.diaryProvider = diaryProvider;
        this.userService = userService;
        this.mealService = mealService;
    }

    public Page<FoodDiaryDTO> getAllDiaries(String username, Pageable pageable) {
        MealooUserDTO user = userService.getUserByUsername(username);
        return diaryProvider.getAllDiaries(user, pageable);
    }

    public FoodDiaryDTO getCurrentDiary(String username) {
        LocalDate currentDate = LocalDate.now();
        MealooUserDTO user = userService.getUserByUsername(username);
        return diaryProvider.getDiaryByDate(user, currentDate);
    }

    public FoodDiaryDTO getDiaryByDate(String username, String textDate) {
        LocalDate date = LocalDate.parse(textDate);
        MealooUserDTO user = userService.getUserByUsername(username);
        return diaryProvider.getDiaryByDate(user, date);
    }

    public FoodDiaryDTO createDiary(String username) {
        MealooUserDTO user = userService.getUserByUsername(username);
        LocalDate currentDate = LocalDate.now();
        return diaryProvider.createDiary(user, currentDate);
    }

    private void recalculateMealMacronutrients(FoodDiaryDTO diary) {
        List<MealDTO> meals = diary.getListOfMeals();
        int totalCarbohydrates = meals.stream().map(MealDTO::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalCarbohydrates).sum();
        int totalProteins = meals.stream().map(MealDTO::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalProteins).sum();
        int totalFats = meals.stream().map(MealDTO::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalFats).sum();
        diary.getMealMacronutrients().setTotalCarbohydrates(totalCarbohydrates);
        diary.getMealMacronutrients().setTotalProteins(totalProteins);
        diary.getMealMacronutrients().setTotalFats(totalFats);
    }

    void recalculatePrice(FoodDiaryDTO diary) {
        List<MealDTO> meals = diary.getListOfMeals();
        double totalPrice = meals.stream().mapToDouble(MealDTO::getPrice).sum();
        diary.setTotalPrice(totalPrice);
    }

    private void recalculateTotalCalories(FoodDiaryDTO diary) {
        List<MealDTO> meals = diary.getListOfMeals();
        int totalCalories = meals.stream().mapToInt(MealDTO::getTotalCalories).sum();
        diary.setTotalCalories(totalCalories);
    }

    public void recalculateData(FoodDiaryDTO diary) {
        recalculateMealMacronutrients(diary);
        recalculatePrice(diary);
        recalculateTotalCalories(diary);

    }

    public FoodDiaryDTO addMeal(String username, String mealName) {
        FoodDiaryDTO diary = getCurrentDiary(username);
        MealDTO meal = mealService.getMealByName(mealName);
        MealooUserDTO user = userService.getUserByUsername(username);

        diary.getListOfMeals().add(meal);
        recalculateData(diary);
        return diaryProvider.updateDiary(diary, user);
    }

    public FoodDiaryDTO deleteMeal(String username, String mealName) {
        FoodDiaryDTO diary = getCurrentDiary(username);
        MealDTO meal = mealService.getMealByName(mealName);
        MealooUserDTO user = userService.getUserByUsername(username);

        diary.getListOfMeals().remove(meal);
        recalculateData(diary);
        return diaryProvider.updateDiary(diary, user);
    }

    List<String> rejectMealsFromThreeDaysBack(MealooUserDTO user) {
        LocalDate threeDaysBack = LocalDate.now().minusDays(3);
        return diaryProvider.rejectMealsFromThreeDaysBack(user, threeDaysBack).stream()
                .flatMap(diary -> diary.getListOfMeals().stream())
                .map(MealDTO::getName).collect(Collectors.toList());
    }

    public List<String> discardMismatchedMeals(List<String> matchingMeals, List<String> rejectedMeals) {
        for (String meal : rejectedMeals) {
            matchingMeals.remove(meal);
        }
        return matchingMeals;
    }

    public void addMatchingMealsToDiary(String username, List<String> namesOfMatchingMeals, int numberOfMeals) {

        Random random = new Random();

        for (int i = 0; i < numberOfMeals; i++) {
            int randomIndex = random.nextInt(namesOfMatchingMeals.size());
            addMeal(username, namesOfMatchingMeals.get(randomIndex));
            namesOfMatchingMeals.remove(randomIndex);
        }
    }


    public FoodDiaryDTO generateDiet(int totalCalories, int numberOfMeals, String username) {
        MealooUserDTO user = userService.getUserByUsername(username);

        if (totalCalories < 0 || totalCalories > 10000) {
            throw new RuntimeException("Total calories should be higher than 0 and less than 10000," +
                    " This app is not created for hulks");
        }
        if (numberOfMeals < 3 || numberOfMeals > 7) {
            throw new RuntimeException("Numbers of meals should vary from 3 to 7");
        }
        if (getCurrentDiary(username).getListOfMeals().size() != 0) {
            throw new RuntimeException("Meals for present day was already created");
        }

        int perfectCaloricValue = totalCalories / numberOfMeals;

        List<String> namesOfMatchingMeals = mealService.findNamesOfMatchingMeals(perfectCaloricValue);

        List<String> rejectedMeals = rejectMealsFromThreeDaysBack(user);

        namesOfMatchingMeals = discardMismatchedMeals(namesOfMatchingMeals, rejectedMeals);

        if (namesOfMatchingMeals.size() < numberOfMeals) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(username, namesOfMatchingMeals, numberOfMeals);

        int deficit = totalCalories - getCurrentDiary(username).getTotalCalories();

        List<String> mealsToFixDeficit = mealService.findNamesOfMatchingMeals(deficit);

        if (mealsToFixDeficit.isEmpty()) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(username, mealsToFixDeficit, 1);

        return getCurrentDiary(username);
    }

    public FoodDiaryDTO generateDiet(int numberOfMeals, WeightGoal weightGoal, String username) {
        MealooUserDTO user = userService.getUserByUsername(username);

        if (numberOfMeals < 3 || numberOfMeals > 7) {
            throw new RuntimeException("Numbers of meals should vary from 3 to 7");
        }
        if (getCurrentDiary(username).getListOfMeals().size() != 0) {
            throw new RuntimeException("Meals for present day was already created");
        }
        int totalCalories = user.getNutritionSettings().getDailyCaloricGoal();

        switch (weightGoal) {
            case LOSTHALFKGPERWEEK:
                totalCalories -= 500;
                break;
            case LOSTQUARTERKGPERWEEK:
                totalCalories -= 250;
                break;
            case MAINTAIN:
                break;
            case GAINQUARTERKGPERWEEK:
                totalCalories += 250;
                break;
            case GAINHALFKGPERWEEK:
                totalCalories += 500;
                break;
            default:
                System.out.println("No weight, or wrong goal has been chosen." +
                        " Calculator will be assigned to weight maintenance");
        }

        int perfectCaloricValue = totalCalories / numberOfMeals;

        List<String> namesOfMatchingMeals = mealService.findNamesOfMatchingMeals(perfectCaloricValue);

        List<String> removedMeals = rejectMealsFromThreeDaysBack(user);

        namesOfMatchingMeals = discardMismatchedMeals(namesOfMatchingMeals, removedMeals);

        if (namesOfMatchingMeals.size() < numberOfMeals) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(username, namesOfMatchingMeals, numberOfMeals);

        int deficit = totalCalories - getCurrentDiary(username).getTotalCalories();

        List<String> fixDeficit = mealService.findNamesOfMatchingMeals(deficit);

        if (fixDeficit.isEmpty()) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(username, namesOfMatchingMeals, 1);

        return getCurrentDiary(username);
    }

}
