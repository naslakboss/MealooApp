package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.entities.*;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.services.MealService;
import codebuddies.MealooApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/meal")
public class MealController {

    @Autowired
    MealService mealService;

    @Autowired
    ProductService productService;

//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB(){
//        Product beef = productService.findByName("Beef");
////        Product bread = productService.findByName("Bread");
//        Product eggs = productService.findByName("Eggs");
////        List<Product> productList = Arrays.asList(beef, eggs);
//
//        Meal meal2 = new Meal(2L,"beefegg", productList, 8, MealDifficulty.MEDIUM);
//        mealService.save(meal2);
//
//    }

    @GetMapping("")
    public ResponseEntity<List<Meal>> findAllMeals(){
        return ResponseEntity.ok(mealService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Meal> findMealByName(@PathVariable String name){
        Meal searchedMeal = mealService.findByName(name);
        return searchedMeal != null ? ResponseEntity.ok(searchedMeal) : ResponseEntity.notFound().build();
    }

    @GetMapping("/details/{name}")
    public ResponseEntity<Map<String, Integer>> findMealDetails(@PathVariable String name){
        Meal searchedMeal = mealService.findByName(name);
        if(searchedMeal == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchedMeal.showMealDetailsByName(searchedMeal));
    }

    @PostMapping("/add")
    public ResponseEntity<Meal> addMeal(@RequestBody @Valid Meal meal){
        if(mealService.existsByName(meal.getName())) throw new EntityAlreadyFoundException("Meal");
        mealService.save(meal);
        return ResponseEntity.created(URI.create("/" + meal.getName())).body(meal);
    }

    @PatchMapping("/patch/{name}")
    public ResponseEntity<Meal> patchMealByName(@PathVariable String name, @Valid @RequestBody Meal meal){
        Meal patchedMeal = mealService.findByName(name);
        return patchedMeal != null ? ResponseEntity.ok(mealService.updateByName(name, meal)) : ResponseEntity.notFound().build();
    }
    @Transactional
    @DeleteMapping("/delete/{name}")
    public ResponseEntity deleteByName(@PathVariable String name){
        if(!mealService.existsByName(name)){
            return ResponseEntity.notFound().build();
        }
        mealService.deleteByName(name);
        return ResponseEntity.ok("Meal " + name + " was successfully deleted from Repository");
    }
}
