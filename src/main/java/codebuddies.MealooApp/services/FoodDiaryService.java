package codebuddies.MealooApp.services;


import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FoodDiaryService {

    @Autowired
    private FoodDiaryRepository foodDiaryRepository;
    @Autowired
    private MealService mealService;

    //todo using only constuctor dependency injection caused cycle error
//    @Autowired
//    public FoodDiaryService(FoodDiaryRepository foodDiaryRepository, MealService mealService, MealooUserService mealooUserService) {
//        this.foodDiaryRepository = foodDiaryRepository;
//        this.mealService = mealService;
//    }

    public FoodDiary save(FoodDiary diary) {
        return foodDiaryRepository.save(diary);
    }

    public List<FoodDiary> findAll() {
        return foodDiaryRepository.findAll();
    }

    public FoodDiary findByDate(LocalDate date) {
        return findByDate(date);
    }

    public List<FoodDiary> findAllDiaries(MealooUser user) {
        return foodDiaryRepository.findAll().stream()
                .filter(foodDiary -> foodDiary.getFakeUser()==user).collect(Collectors.toList());
    }

    public FoodDiary createNewDiary(MealooUser user){
        LocalDate date = LocalDate.now();
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

    public FoodDiary findTodaysDiary(MealooUser user) {
       LocalDate date = LocalDate.now();
       Optional<FoodDiary> todayFoodDiary = findAllDiaries(user).stream()
               .filter(foodDiary -> foodDiary.getDate().isEqual(date)).findAny();
       if(!todayFoodDiary.isPresent()){
           return createNewDiary(user);
       }
       else{
           return todayFoodDiary.get();
       }
    }

    public FoodDiary addMealToCurrentDiary(MealooUser user, String name) {
        FoodDiary diary = findTodaysDiary(user);
        Meal meal = mealService.findByName(name);

        diary.addMeal(meal);
        diary.setMacronutrients(diary.calculateMacronutrients());
        diary.setTotalCalories(diary.calculateCalories());
        diary.setTotalPrice(diary.calculatePrice());

        foodDiaryRepository.save(diary);
        return diary;


    }

    public FoodDiary findDiaryOfDay(MealooUser user, String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        Optional<FoodDiary> diary = findAllDiaries(user).stream()
                .filter(foodDiary -> foodDiary.getDate().isEqual(parsedDate)).findAny();
        if(diary.isEmpty()){
            createNewDiary(user);
        }
        return diary.get();
    }


    public FoodDiary deleteMealFromCurrentDiary(MealooUser user, String mealName) {
        FoodDiary diary = findTodaysDiary(user);
        Meal meal = mealService.findByName(mealName);
        diary.deleteMeal(meal);

        diary.setMacronutrients(diary.calculateMacronutrients());
        diary.setTotalCalories(diary.calculateCalories());
        diary.setTotalPrice(diary.calculatePrice());

        foodDiaryRepository.save(diary);
        return diary;
    }

}
