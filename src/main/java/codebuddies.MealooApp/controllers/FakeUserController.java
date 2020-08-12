package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.services.FakeUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import codebuddies.MealooApp.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
public class FakeUserController {

    @Autowired
    FakeUserService fakeUserService;

    @Autowired
    MealService mealService;

    @Autowired
    FoodDiaryService foodDiaryService;

    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB(){
//        Meal mealo = mealService.findByName("beefegg");
//
//        FakeUser user1 = fakeUserService.findByUsername("naslakboss");
//
//        LocalDate date1 = LocalDate.of(2020, 8,11);
//        FoodDiary diary1 = new FoodDiary( Collections.singletonList(mealo), date1, user1);
//        foodDiaryService.save(diary1);
//        user1.setFoodDiary(diary1);
//        FakeUserDetails user1details = new FakeUserDetails(183, 93, 21, Sex.MALE, PhysicalActivity.HIGH);
//        user1.setFakeUserDetails(user1details);
//        fakeUserService.save(user1);
//    }

    @GetMapping("")
    public List<FakeUser> findAllUsers(){
        return fakeUserService.findAll();
    }

    @GetMapping("/givemelistofproductsforapi")
    public Object getMealsAndRecipesForWholeDay(){
        return fakeUserService.getMealsAndRecipes();
    }
}
