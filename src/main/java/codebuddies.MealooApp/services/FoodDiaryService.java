package codebuddies.MealooApp.services;


import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Service
public class FoodDiaryService {

    @Autowired
    FoodDiaryRepository foodDiaryRepository;

    @Autowired
    MealService mealService;

    @Autowired
    FakeUserService userService;

    public FoodDiary save(FoodDiary diary) {
        return foodDiaryRepository.save(diary);
    }

    public List<FoodDiary> findAll() {
        return foodDiaryRepository.findAll();
    }

    public FoodDiary findByDate(LocalDate date) {
        return findByDate(date);
    }

    public List<FoodDiary> findAllDiariesForUser(FakeUser user) {

        return foodDiaryRepository.findAll().stream()
                .filter(foodDiary -> foodDiary.getFakeUser()==user).collect(Collectors.toList());
    }

    public FoodDiary createNewFoodDiary(FakeUser user){
        LocalDate date = LocalDate.now();
        Optional<FoodDiary> checkIfExists = findAllDiariesForUser(user).stream()
                .filter(foodDiary -> foodDiary.getDate().isEqual(date)).findFirst();

        if(checkIfExists.isPresent()){
            throw new RuntimeException ("Food diary for this day is already exists!");
        }
        else {
            FoodDiary newFoodDiary = new FoodDiary(Collections.emptyList(), date, user);
            Random random = new Random();
            Long idBean = (long)random.nextInt(500);
            newFoodDiary.setId((idBean));
            newFoodDiary.setTotalPrice(0);
            newFoodDiary.setTotalCalories(0);
            newFoodDiary.setMacronutrients(new Macronutrients(0,0,0));
            foodDiaryRepository.save(newFoodDiary);
            return newFoodDiary;
        }

    }

    public FoodDiary findTodayDiary(FakeUser user) {
       LocalDate date = LocalDate.now();
       Optional<FoodDiary> todayFoodDiary = findAllDiariesForUser(user).stream()
               .filter(foodDiary -> foodDiary.getDate().isEqual(date)).findFirst();
       if(todayFoodDiary.isPresent()){
           return todayFoodDiary.get();
       }
       else {
           throw new RuntimeException("Diary for today is not exists yet");
       }
    }

    public FoodDiary addFoodToTodayDiary(FakeUser user, Meal meal) {
        FoodDiary diary = findTodayDiary(user);
        diary.getListOfMeals().add(meal);

        diary.setMacronutrients(diary.calculateMacronutrients());
        diary.setTotalCalories(diary.calculateCalories());
        diary.setTotalPrice(diary.calculatePrice());
        foodDiaryRepository.save(diary);
        return diary;
    }

    public FoodDiary deleteMealFromTodayDiary(FakeUser user, Meal mealToDelete) {
        FoodDiary diary = findTodayDiary(user);
        diary.getListOfMeals().remove(mealToDelete);

        diary.setMacronutrients(diary.calculateMacronutrients());
        diary.setTotalCalories(diary.calculateCalories());
        diary.setTotalPrice(diary.calculatePrice());

        foodDiaryRepository.save(diary);
        return diary;
    }

    public FoodDiary findDiaryOfGivenDate(FakeUser user, String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        Optional<FoodDiary> diary = findAllDiariesForUser(user).stream()
                .filter(foodDiary -> foodDiary.getDate().isEqual(parsedDate)).findFirst();
        if(diary.isEmpty()){
            throw new IllegalArgumentException("Dairy of given date is not exist");
        }
        return diary.get();
    }
}
