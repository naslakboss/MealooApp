package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.repositories.FakeUserRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class FakeUserService {

    @Autowired
    FakeUserRepository fakeUserRepository;
    // Wstrzykiwanie przez konstruktor

    @Autowired
    MealService mealService;

    @Autowired
    FoodDiaryService foodDiaryService;

    public List<FakeUser> findAll() {
        return fakeUserRepository.findAll();
    }

    public FakeUser save(FakeUser user1) {
        return fakeUserRepository.save(user1);
    }

    public FakeUser findByUsername(String username) {
        return fakeUserRepository.findByUsername(username);
    }

    public FakeUser addMealToDiary(String username, Meal meal, LocalDate date) {
        FakeUser user = fakeUserRepository.findByUsername(username);

        FoodDiary foodDiary = foodDiaryService.findByDate(date);

        user.addDiary(foodDiary);

        foodDiary.setListOfMeals(Collections.singletonList(meal));

        fakeUserRepository.save(user);

        foodDiaryService.save(foodDiary);

        return user;
    }


}
