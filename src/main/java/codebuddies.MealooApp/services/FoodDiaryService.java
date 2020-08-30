package codebuddies.MealooApp.services;


import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        FoodDiary newFoodDiary = new FoodDiary(Collections.emptyList(), date, user);
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

}
