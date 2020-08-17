package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.MealDTO;
import codebuddies.MealooApp.dataProviders.MealFacade;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.product.Product;
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

    @Autowired
    MealFacade mealFacade;

//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB(){
//        Product bread = productService.findByName("Bread");
//        Product egg = productService.findByName("Eggs");
//        Product pesta = productService.findByName("Pesta");
//        Product beef = productService.findByName("Beef");
//        Product strawberry = productService.findByName("Strawberry");
//
//        List<Product> products1 = Arrays.asList(bread, egg);
//        Meal meal1 = new Meal(13L, "BreadWithEgg", products1, 15, MealDifficulty.EASY);
//        mealService.save(meal1);
//
//        List<Product> products2 = Arrays.asList(pesta, beef);
//        Meal meal2 = new Meal(14L, "PastaAndBeef", products2, 20, MealDifficulty.EASY);
//        mealService.save(meal2);
//
//        List<Product> products3 = Arrays.asList(pesta, strawberry);
//        Meal meal3 = new Meal(15L, "PastaWithStrawberries", products3, 15, MealDifficulty.EASY);
//        mealService.save(meal3);
//    }

    @GetMapping("")
    public ResponseEntity<List<MealDTO>> findAllMeals(){
        return ResponseEntity.ok(mealFacade.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<MealDTO> findMealByName(@PathVariable String name){
        MealDTO searchedMeal = mealFacade.getByName(name);
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
