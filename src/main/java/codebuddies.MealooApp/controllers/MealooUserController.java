package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.user.*;

import codebuddies.MealooApp.services.MealooUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/users")
//@PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
public class MealooUserController {


    private MealooUserService userService;

    public MealooUserController(MealooUserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<Page<MealooUser>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{username}")
    public ResponseEntity<MealooUser> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/add")
    public ResponseEntity<MealooUser> createUser(@Valid @RequestBody MealooUser user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{username}")
    public ResponseEntity<MealooUser> updateUser
            (@Valid @RequestBody MealooUser user, @PathVariable String username){
        return ResponseEntity.ok(userService.updateUserByUsername(user, username));

    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity deleteUser(@PathVariable String username){
        userService.deleteByUsername(username);
        return ResponseEntity.ok("User " + username + " was successfully deleted from database");
    }

    @GetMapping("/{username}/calculate")
    public ResponseEntity<Map> calculateBMI(@PathVariable String username) {
        return ResponseEntity.ok(userService.calculateBMIAndCaloricDemand(username));
    }


}

