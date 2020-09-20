package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.entities.user.WeightGoal;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FoodDiaryService {


    private FoodDiaryRepository foodDiaryRepository;

    private MealService mealService;

    private MealooUserService mealooUserService;


    @Autowired
    public FoodDiaryService(FoodDiaryRepository foodDiaryRepository, MealService mealService, MealooUserService mealooUserService) {
        this.foodDiaryRepository = foodDiaryRepository;
        this.mealService = mealService;
        this.mealooUserService = mealooUserService;
    }

    public FoodDiary save(FoodDiary diary) {
        return foodDiaryRepository.save(diary);
    }

    public List<FoodDiary> findAll() {
        return foodDiaryRepository.findAll();
    }

    public List<FoodDiary> findByDate(LocalDate date) {
        if (foodDiaryRepository.findByDate(date).isEmpty()) {
            throw new ResourceNotFoundException("Diary of Given date does not exist in database");
        }
        return foodDiaryRepository.findByDate(date).get();
    }

    public List<FoodDiary> findAllDiariesPageable(MealooUser user, Pageable pageable) {
        return foodDiaryRepository.findAll(pageable).stream()
                .filter(foodDiary -> foodDiary.getMealooUser() == user).collect(Collectors.toList());
    }

    public List<FoodDiary> findAllDiaries(MealooUser user) {
        return foodDiaryRepository.findAll().stream()
                .filter(foodDiary -> foodDiary.getMealooUser() == user).collect(Collectors.toList());
    }

    public FoodDiary createNewDiary(MealooUser user) {
        LocalDate date = LocalDate.now();
        List<Meal> emptyList = new ArrayList<>();
        FoodDiary newFoodDiary = new FoodDiary(emptyList, date, user);
        newFoodDiary.setTotalPrice(0);
        newFoodDiary.setTotalCalories(0);
        newFoodDiary.setMealMacronutrients(new MealMacronutrients(0, 0, 0));

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
        if (diary.isEmpty()) {
            return createNewDiary(user);
        }
        return diary.get();
    }

    public void recalculateData(FoodDiary diary) {
        diary.setMealMacronutrients(diary.calculateMealMacronutrients());
        diary.setTotalCalories(diary.calculateCalories());
        diary.setTotalPrice(diary.calculatePrice());
    }

    public FoodDiary addMealToCurrentDiary(String username, String name) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(username);
        FoodDiary diary = findTodaysDiary(user);
        Meal meal = mealService.findByName(name);

        diary.addMeal(meal);
        recalculateData(diary);

        foodDiaryRepository.save(diary);
        return diary;
    }

    public FoodDiary deleteMealFromCurrentDiary(String username, String mealName) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(username);
        FoodDiary diary = findTodaysDiary(user);
        Meal meal = mealService.findByName(mealName);

        diary.deleteMeal(meal);
        recalculateData(diary);

        foodDiaryRepository.save(diary);
        return diary;
    }

    public List<String> rejectMealsFromThreeDaysBack(MealooUser user) {

        return findAllDiaries(user).stream()
                .filter(time -> DAYS.between(time.getDate(), LocalDate.now()) <= 3)
                    .map(FoodDiary::getListOfMeals)
                        .flatMap(Collection::stream)
                            .map(Meal::getName)
                                .collect(Collectors.toList());
    }

    public FoodDiary generateDiet(int totalCalories, int numbersOfMeals, String username) {
        MealooUser user = mealooUserService.findByUsername(username);

        if (totalCalories < 0 || totalCalories > 10000) {
            throw new RuntimeException("Total calories should be higher than 0 and less than 10000," +
                    " This app is not created for hulks");
        }
        if (numbersOfMeals < 3 || numbersOfMeals > 7) {
            throw new RuntimeException("Numbers of meals should vary from 3 to 7");
        }
        if (findTodaysDiary(user).getListOfMeals().size() != 0) {
            throw new RuntimeException("Meals for present day was already created");
        }
        int perfectCaloricValue = totalCalories / numbersOfMeals;

        List<String> namesOfMatchingMeals = mealService.findAllNamesOfMatchingMeals(perfectCaloricValue);

        List<String> removedMeals = rejectMealsFromThreeDaysBack(user);

        for (String meal : removedMeals) {
            namesOfMatchingMeals.remove(meal);
        }

        if (namesOfMatchingMeals.size() < numbersOfMeals) {
            throw new ResourceNotFoundException("Sorry, database does not contain required meals." +
                    " Try to add new meals or create your own diary manually");
        }

        Random random = new Random();

        for (int i = 0; i < numbersOfMeals - 1; i++) {
            int randomIndex = random.nextInt(namesOfMatchingMeals.size());
            addMealToCurrentDiary(username, namesOfMatchingMeals.get(randomIndex));
            namesOfMatchingMeals.remove(randomIndex);
        }

        int deficit = totalCalories - findTodaysDiary(user).getTotalCalories();

        List<String> fixDeficit = mealService.findAllNamesOfMatchingMeals(deficit);

        if (fixDeficit.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, database does not contain required meals." +
                    " Try to add new meals or create your own diary manually");
        }

        addMealToCurrentDiary(username, fixDeficit.get(random.nextInt(fixDeficit.size())));

        return findTodaysDiary(user);
    }

    public FoodDiary generateDiet(int numbersOfMeals, WeightGoal weightGoal, String username) {
        MealooUser user = mealooUserService.findByUsername(username);

        if (numbersOfMeals < 3 || numbersOfMeals > 7) {
            throw new RuntimeException("Numbers of meals should vary from 3 to 7");
        }
        if (findTodaysDiary(user).getListOfMeals().size() != 0) {
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
                        " Calculator will be assigned weight maintenance");
        }
        int perfectCaloricValue = totalCalories / numbersOfMeals;

        List<String> namesOfMatchingMeals = mealService.findAllNamesOfMatchingMeals(perfectCaloricValue);

        List<String> removedMeals = rejectMealsFromThreeDaysBack(user);

        for (String meal : removedMeals) {
            namesOfMatchingMeals.remove(meal);
        }

        if (namesOfMatchingMeals.size() < numbersOfMeals) {
            throw new ResourceNotFoundException("Sorry, database does not contain required meals." +
                    " Try to add new meals or create your own diary manually");
        }

        Random random = new Random();

        for (int i = 0; i < numbersOfMeals - 1; i++) {
            int randomIndex = random.nextInt(namesOfMatchingMeals.size());
            addMealToCurrentDiary(username, namesOfMatchingMeals.get(randomIndex));
            namesOfMatchingMeals.remove(randomIndex);
        }

        int deficit = totalCalories - findTodaysDiary(user).getTotalCalories();

        List<String> fixDeficit = mealService.findAllNamesOfMatchingMeals(deficit);

        if (fixDeficit.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, database does not contain required meals." +
                    " Try to add new meals or create your own diary manually");
        }

        addMealToCurrentDiary(username, fixDeficit.get(random.nextInt(fixDeficit.size())));

        return findTodaysDiary(user);
    }

}
