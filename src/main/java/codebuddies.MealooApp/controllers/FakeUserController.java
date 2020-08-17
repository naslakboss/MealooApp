package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.services.FakeUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import codebuddies.MealooApp.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
    @EventListener(ApplicationReadyEvent.class)
    public void fillDB(){

//        FakeUser fakeUser = new FakeUser(2L, "test", "pass", "test@gmail.com"
//                    ,new NutritionSettings(4000)
//                        , new FakeUserDetails(100, 50, 30, Sex.MALE, PhysicalActivity.HIGH));
//        fakeUserService.save(fakeUser);
//     FakeUser user = fakeUserService.findByUsername("test");
//        Meal mealo = mealService.findByName("strawmilkoats");
//
//        LocalDate date = LocalDate.of(2020, 8,11);
//        FoodDiary diary1 = new FoodDiary( Collections.singletonList(mealo), date, user);
//        foodDiaryService.save(diary1);




     }

    @GetMapping("")
    public List<FakeUser> findAllUsers(){
        return fakeUserService.findAll();
    }





}

