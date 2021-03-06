package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.services.MealooUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or authentication.principal.username == #username")
public class MealooUserController {


    private final MealooUserService userService;

    public MealooUserController(MealooUserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<Page<MealooUserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{username}")
    public ResponseEntity<MealooUserDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MealooUserDTO> createUser(@Valid @RequestBody MealooUserDTO user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{username}")
    public ResponseEntity<MealooUserDTO> updateUser
            (@Valid @RequestBody MealooUserDTO user, @PathVariable String username) {
        return ResponseEntity.ok(userService.updateUserByUsername(user, username));

    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.ok("User " + username + " was successfully deleted from database");
    }

    @GetMapping("/{username}/calculate")
    public ResponseEntity<Map<String, Double>> calculateBMI(@PathVariable String username) {
        return ResponseEntity.ok(userService.giveBMIAndCaloricDemandInformation(username));
    }


}

