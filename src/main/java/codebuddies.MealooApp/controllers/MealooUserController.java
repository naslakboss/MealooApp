package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.services.MealooUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class MealooUserController {


    private MealooUserService mealooUserService;

    @Autowired
    public MealooUserController(MealooUserService mealooUserService) {
        this.mealooUserService = mealooUserService;
    }

//    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB(){
//        MealooUser fakeUser = new MealooUser(2L, "test", "pass", "test@gmail.com"
//                    ,new NutritionSettings(4000)
//                        , new MealooUserDetails(100, 50, 30, Sex.MALE, PhysicalActivity.HIGH));
//        mealooUserService.save(fakeUser);
//        MealooUser naslakboss = new MealooUser(3L, "naslakboss", "pass", "damian@gmail.com"
//                ,new NutritionSettings(3700)
//                , new MealooUserDetails(183, 93, 21, Sex.MALE, PhysicalActivity.MEDIUM));
//        mealooUserService.save(naslakboss);
//     }

    @GetMapping("")
    public List<MealooUser> findAllUsers(){
        return mealooUserService.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<MealooUser> findUserByUsername(@PathVariable String username){
        MealooUser user = mealooUserService.findByUsername(username);
        return  user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{username}")
    public ResponseEntity<MealooUser> patchUserByUsername(@PathVariable String username
            , @RequestBody MealooUser mealooUser){
        MealooUser user = mealooUserService.findByUsername(username);
        if(user == null){
            ResponseEntity.notFound().build();
        }

        MealooUser patchedUser = mealooUserService.patchByUsername(username, mealooUser);
        return ResponseEntity.ok(patchedUser);

    }
    @GetMapping("/calculateBMIandCaloricDemand/{username}")
    public ResponseEntity<Map> calculateBMI(@PathVariable String username){
        MealooUser user = mealooUserService.findByUsername(username);
        if(user == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mealooUserService.calculateBMIandCaloricDemand(user));
    }
}

