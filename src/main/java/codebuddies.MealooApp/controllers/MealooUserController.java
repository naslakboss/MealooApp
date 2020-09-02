package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
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
    public ResponseEntity<List<MealooUser>> findAllUsers(){
        return ResponseEntity.ok(mealooUserService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<MealooUser> findUserByUsername(@PathVariable String username) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<MealooUser> patchUserByUsername(@PathVariable String username
            , @RequestBody MealooUser mealooUser){
        MealooUser patchedUser = mealooUserService.updateByUsername(username, mealooUser);
        return ResponseEntity.ok(patchedUser);

    }
    @GetMapping("/calculateBMIandCaloricDemand/{username}")
    public ResponseEntity<Map> calculateBMI(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(mealooUserService.calculateBMIAndCaloricDemand(mealooUserService.findByUsername(username)));
    }

}

