package codebuddies.MealooApp.services;


import codebuddies.MealooApp.dataProviders.FoodDiaryDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FoodDiaryService {



    FoodDiaryRepository foodDiaryRepository;

    MealService mealService;

    @Autowired
    public FoodDiaryService(FoodDiaryRepository foodDiaryRepository, MealService mealService) {
        this.foodDiaryRepository = foodDiaryRepository;
        this.mealService = mealService;
    }

    public FoodDiary save(FoodDiary diary) {
        return foodDiaryRepository.save(diary);
    }

    public List<FoodDiary> findAll() {
        return foodDiaryRepository.findAll();
    }

    public List<FoodDiary> findByDate(LocalDate date) {
        if(foodDiaryRepository.findByDate(date).isEmpty()){
            throw new ResourceNotFoundException("Diary of Given date does not exist in database");
        }
        return foodDiaryRepository.findByDate(date).get();
    }

    public List<FoodDiary> findAllDiaries(MealooUser user) {
        return foodDiaryRepository.findAll().stream()
                .filter(foodDiary -> foodDiary.getMealooUser()==user).collect(Collectors.toList());
    }

    public FoodDiary createNewDiary(MealooUser user){
        LocalDate date = LocalDate.now();
        List<Meal> emptyList = new ArrayList<>();
        FoodDiary newFoodDiary = new FoodDiary(emptyList, date, user);
        newFoodDiary.setTotalPrice(0);
        newFoodDiary.setTotalCalories(0);
        newFoodDiary.setMealMacronutrients(new MealMacronutrients(0,0,0));

        foodDiaryRepository.save(newFoodDiary);
        return newFoodDiary;
    }

    public FoodDiary findTodaysDiary(MealooUser user) {
       LocalDate date = LocalDate.now();
       Optional<FoodDiary> todayFoodDiary = findAllDiaries(user).stream()
               .filter(foodDiary -> foodDiary.getDate().isEqual(date)).findAny();
        return todayFoodDiary.orElseGet(() -> createNewDiary(user));
    }

    public FoodDiary findDiaryOfDay(MealooUser user, String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        Optional<FoodDiary> diary = findAllDiaries(user).stream()
                .filter(foodDiary -> foodDiary.getDate().isEqual(parsedDate)).findAny();
        if(diary.isEmpty()){
            return createNewDiary(user);
        }
        return diary.get();
    }

    public FoodDiary addMealToCurrentDiary(MealooUser user, String name) throws ResourceNotFoundException {
        FoodDiary diary = findTodaysDiary(user);
        Meal meal = mealService.findByName(name);

        diary.addMeal(meal);
        diary.setMealMacronutrients(diary.calculateMealMacronutrients());
        diary.setTotalCalories(diary.calculateCalories());
        diary.setTotalPrice(diary.calculatePrice());

        foodDiaryRepository.save(diary);
        return diary;
    }

    public FoodDiary deleteMealFromCurrentDiary(MealooUser user, String mealName) throws ResourceNotFoundException {
        FoodDiary diary = findTodaysDiary(user);
        Meal meal = mealService.findByName(mealName);
        diary.deleteMeal(meal);

        diary.setMealMacronutrients(diary.calculateMealMacronutrients());
        diary.setTotalCalories(diary.calculateCalories());
        diary.setTotalPrice(diary.calculatePrice());

        foodDiaryRepository.save(diary);
        return diary;
    }

    public FoodDiary generateDiet(int totalCalories, int numbersOfMeals, MealooUser user) {
        if(totalCalories < 0 || totalCalories > 10000){
            throw new RuntimeException("Total calories should be higher than 0 and less than 10000," +
                    " This app is not created for hulks");
        }
        if(numbersOfMeals < 3 || numbersOfMeals > 7){
            throw new RuntimeException("Numbers of meals should vary from 3 to 7");
        }
        if(findTodaysDiary(user).getListOfMeals().size() != 0){
            throw new RuntimeException("Meals for present day was already created");
        }
        int perfectCaloricValue = totalCalories/numbersOfMeals;

        List<String> namesOfMatchingMeals = mealService.findAllNamesOfMatchingMeals(perfectCaloricValue);

        List<String> removedMeals = rejectMealsFromThreeDaysBack(user);

        for(String meal : removedMeals){
            namesOfMatchingMeals.remove(meal);
        }

        if(namesOfMatchingMeals.size() < numbersOfMeals){
            throw new ResourceNotFoundException("Sorry, database does not contain required meals." +
                    " Try to add new meals or create your own diary manually");
        }

        Random random = new Random();

        for(int i = 0; i < numbersOfMeals - 1; i++){
            int randomIndex = random.nextInt(namesOfMatchingMeals.size());
            addMealToCurrentDiary(user, namesOfMatchingMeals.get(randomIndex));
            namesOfMatchingMeals.remove(randomIndex);
        }

        int deficit = totalCalories - findTodaysDiary(user).getTotalCalories();

        List<String> fixDeficit = mealService.findAllNamesOfMealsForCorrectDeficit(deficit);

        if(fixDeficit.isEmpty()){
            throw new ResourceNotFoundException("Sorry, database does not contain required meals." +
                    " Try to add new meals or create your own diary manually");
        }

        addMealToCurrentDiary(user, fixDeficit.get(random.nextInt(fixDeficit.size())));

        return findTodaysDiary(user);
    }

    public List<String> rejectMealsFromThreeDaysBack(MealooUser user){

        List<String> result = user.getFoodDiaries().stream()
                .filter(time -> DAYS.between(time.getDate(), LocalDate.now()) <= 3)
                .map(FoodDiary::getListOfMeals)
                .flatMap(Collection::stream)
                .map(Meal::getName)
                .collect(Collectors.toList());
        return result;
    }
}
