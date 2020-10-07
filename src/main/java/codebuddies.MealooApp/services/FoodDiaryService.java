package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.FoodDiaryMapper;
import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.user.WeightGoal;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.exceptions.RequiredMealsNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.security.SecureRandom.getInstanceStrong;

@Service
public class FoodDiaryService {

    private final Random rand = getInstanceStrong();

    private final FoodDiaryMapper diaryMapper;

    private final MealooUserService userService;

    private final MealService mealService;

    public FoodDiaryService(FoodDiaryMapper diaryMapper, MealooUserService userService, MealService mealService) throws NoSuchAlgorithmException {
        this.diaryMapper = diaryMapper;
        this.userService = userService;
        this.mealService = mealService;
    }

    public Page<FoodDiaryDTO> getAllDiaries(int id, Pageable pageable) {
        return diaryMapper.getAllDiaries(id, pageable);
    }

    public boolean existsByDate(int id, LocalDate date){
        return diaryMapper.existsByDate(id, date);
    }

    public FoodDiaryDTO getCurrentDiary(int id) {
        LocalDate currentDate = LocalDate.now();
        return diaryMapper.getDiaryByDate(id, currentDate);
    }

    public FoodDiaryDTO getDiaryByDate(int id, String textDate) {
        LocalDate date = LocalDate.parse(textDate);
        return diaryMapper.getDiaryByDate(id, date);
    }

    public FoodDiaryDTO createDiary(int id) {
        LocalDate currentDate = LocalDate.now();
        if(existsByDate(id, currentDate)){
            throw new EntityAlreadyFoundException("Diary for user with ID :" + id);
        }
        MealooUserDTO user = userService.getUserById(id);
        return diaryMapper.createDiary(currentDate, user);
    }

    int recalculateProteins(List<MealDTO> meals){
        return meals.stream().map(MealDTO::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalProteins).sum();
    }

    int recalculateCarbohydrates(List<MealDTO> meals){
        return meals.stream().map(MealDTO::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalCarbohydrates).sum();
    }

    int recalculateFats(List<MealDTO> meals){
        return meals.stream().map(MealDTO::getMealMacronutrients)
                .mapToInt(MealMacronutrients::getTotalFats).sum();
    }

    private void recalculateMealMacronutrients(FoodDiaryDTO diary) {
        List<MealDTO> meals = diary.getListOfMeals();
        int totalProteins = recalculateProteins(meals);
        int totalCarbohydrates = recalculateCarbohydrates(meals);
        int totalFats = recalculateFats(meals);
        diary.setMealMacronutrients(new MealMacronutrients(totalProteins, totalCarbohydrates, totalFats));
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

    public FoodDiaryDTO addMeal(int id, String mealName) {
        FoodDiaryDTO diary = getCurrentDiary(id);
        MealDTO meal = mealService.getMealByName(mealName);
        diary.addMeal(meal);
        recalculateData(diary);
        return diaryMapper.updateDiary(diary);
    }

    public FoodDiaryDTO deleteMeal(int id, String mealName) {
        FoodDiaryDTO diary = getCurrentDiary(id);
        MealDTO meal = mealService.getMealByName(mealName);
        diary.deleteMeal(meal);
        recalculateData(diary);
        return diaryMapper.updateDiary(diary);
    }

    public void addMatchingMealsToDiary(int id, List<String> namesOfMatchingMeals, int numberOfMeals) {

        for (int i = 0; i < numberOfMeals - 1; i++) {
            int randomIndex = rand.nextInt(namesOfMatchingMeals.size());
            addMeal(id, namesOfMatchingMeals.get(randomIndex));
            namesOfMatchingMeals.remove(randomIndex);
        }
    }

    public void validateInputGenerator(int numberOfMeals, int totalCalories, int id)
    {
        if (totalCalories < 0 || totalCalories > 10000) {
            throw new IllegalArgumentException("Total calories should be higher than 0 and less than 10000," +
                    " This app is not created for hulks");
        }
        if (numberOfMeals < 3 || numberOfMeals > 7) {
            throw new IllegalArgumentException("Numbers of meals should vary from 3 to 7");
        }
        if (!getCurrentDiary(id).getListOfMeals().isEmpty()){
            throw new IllegalArgumentException("Meals for present day was already created");
        }
    }

    public int calculatePerfectCaloricValue(int totalCalories, int numberOfMeals){
        return totalCalories / numberOfMeals;
    }

    List<String> rejectMealsFromThreeDaysBack(int id) {
        LocalDate threeDaysBack = LocalDate.now().minusDays(3);
        return diaryMapper.rejectMealsFromThreeDaysBack(id, threeDaysBack).stream()
                .flatMap(diary -> diary.getListOfMeals().stream())
                .map(MealDTO::getName).collect(Collectors.toList());
    }

    public List<String> discardMismatchedMeals(int perfectCaloricValue, int id) {
        List<String> namesOfMatchingMeals = mealService.findNamesOfMatchingMeals(perfectCaloricValue);

        List<String> rejectedMeals = rejectMealsFromThreeDaysBack(id);

        for (String meal : rejectedMeals) {
            namesOfMatchingMeals.remove(meal);
        }
        return namesOfMatchingMeals;
    }

    int calculateDeficitForCorrectiveMeal(int totalCalories, int id){
        return totalCalories - getCurrentDiary(id).getTotalCalories();
    }

    public FoodDiaryDTO generateDiet(int numberOfMeals, int totalCalories, int id) {

        validateInputGenerator(numberOfMeals, totalCalories ,id);

        int perfectCaloricValue = calculatePerfectCaloricValue(totalCalories, numberOfMeals);

        List<String> namesOfMatchingMeals = discardMismatchedMeals(perfectCaloricValue, id);

        if (namesOfMatchingMeals.size() < numberOfMeals) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(id, namesOfMatchingMeals, numberOfMeals);

        int deficit = calculateDeficitForCorrectiveMeal(totalCalories, id);

        List<String> mealsToFixDeficit = mealService.findNamesOfMatchingMeals(deficit);

        if (mealsToFixDeficit.isEmpty()) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(id, mealsToFixDeficit, 2);

        return getCurrentDiary(id);
    }

    public int calculateTotalCaloriesAccordingToWeightGoal(WeightGoal weightGoal, int totalCalories){
        switch (weightGoal) {
            case LOSEHALFKGPERWEEK:
                totalCalories -= 500;
                break;
            case LOSEQUARTERKGPERWEEK:
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
        return totalCalories;
    }

    public FoodDiaryDTO generateDiet(int numberOfMeals, int totalCalories, WeightGoal weightGoal, int id) {

        validateInputGenerator(numberOfMeals, totalCalories, id);

        totalCalories = calculateTotalCaloriesAccordingToWeightGoal(weightGoal, totalCalories);

        int perfectCaloricValue = calculatePerfectCaloricValue(totalCalories, numberOfMeals);

        List<String> namesOfMatchingMeals = discardMismatchedMeals(perfectCaloricValue, id);

        if (namesOfMatchingMeals.size() < numberOfMeals) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(id, namesOfMatchingMeals, numberOfMeals);

        int deficit = calculateDeficitForCorrectiveMeal(totalCalories, id);

        List<String> fixDeficit = mealService.findNamesOfMatchingMeals(deficit);

        if (fixDeficit.isEmpty()) {
            throw new RequiredMealsNotFoundException("");
        }

        addMatchingMealsToDiary(id, namesOfMatchingMeals, 2);

        return getCurrentDiary(id);
    }


}
