package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.MealooUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
public class MealooUserController {


    private MealooUserService mealooUserService;


    @Autowired
    public MealooUserController(MealooUserService mealooUserService) {
        this.mealooUserService = mealooUserService;
    }

//    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB(){
////        MealooUser fakeUser = new MealooUser(2L, "test", "pass", "test@gmail.com"
////                    ,new NutritionSettings(4000)
////                        , new MealooUserDetails(100, 50, 30, Sex.MALE, PhysicalActivity.HIGH));
////        mealooUserService.save(fakeUser);
////        MealooUser client = new MealooUser(3L, "client", "pass", "client@gmail.com"
////                ,new NutritionSettings(2000)
////                , new MealooUserDetails(170, 65, 28, Sex.MALE, PhysicalActivity.MEDIUM));
////        mealooUserService.save(client);
//        MealooUser admin = mealooUserService.findByUsername("naslakboss");
//        admin.setMealooUserRole(MealooUserRole.ADMIN);
//
//        MealooUser client = mealooUserService.findByUsername("client");
//        client.setMealooUserRole(MealooUserRole.MODERATOR);
//
//        MealooUser test = mealooUserService.findByUsername("test");
//        test.setMealooUserRole(MealooUserRole.USER);
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
    @GetMapping("/{username}/calculate")
    public ResponseEntity<Map> calculateBMI(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(mealooUserService.calculateBMIAndCaloricDemand(mealooUserService.findByUsername(username)));
    }

}

