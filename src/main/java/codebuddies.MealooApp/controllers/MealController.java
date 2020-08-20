package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.MealDTO;
import codebuddies.MealooApp.dataProviders.MealFacade;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.repositories.IngredientRepository;
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

    private MealService mealService;


    private MealFacade mealFacade;

    private ProductService productService;

    private IngredientRepository ingredientRepository;

    @Autowired
    public MealController(MealService mealService, MealFacade mealFacade,
                          ProductService productService, IngredientRepository ingredientRepository) {
        this.mealService = mealService;
        this.mealFacade = mealFacade;
        this.productService = productService;
        this.ingredientRepository = ingredientRepository;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void testNewMeal(){
//
//        Product bread = productService.findByName("Bread");
//        Product egg = productService.findByName("Egg");
//        Product milk = productService.findByName("Milk");
//        Product beef = productService.findByName("Beef");
//        Product chicken = productService.findByName("Chicken");
//        Product pasta = productService.findByName("Pasta");
//        Product strawberry = productService.findByName("Strawberry");
//
//        Ingredient breadI = new Ingredient(300, bread);
//        Ingredient milkI = new Ingredient(500, milk);
//        Ingredient beefI = new Ingredient(400, beef);
//        Ingredient chickenI = new Ingredient(600, chicken);
//        Ingredient pastaI = new Ingredient(200, pasta);
//        Ingredient strawberryI = new Ingredient(1000, strawberry);
//
//        ingredientRepository.save(breadI);
//        ingredientRepository.save(milkI);
//        ingredientRepository.save(beefI);
//        ingredientRepository.save(chickenI);
//        ingredientRepository.save(pastaI);
//        ingredientRepository.save(strawberryI);
//
//        Ingredient milkIn = ingredientRepository.findById(milkI.getId()).get();
//        Ingredient beefIn = ingredientRepository.findById(beefI.getId()).get();
//        Ingredient chickenIn = ingredientRepository.findById(chickenI.getId()).get();
//        Ingredient pastaIn = ingredientRepository.findById(pastaI.getId()).get();
//        Ingredient strawberryIn = ingredientRepository.findById(strawberryI.getId()).get();
//
//        List<Ingredient>  pastaAndChicken = Arrays.asList(pastaIn,chickenIn);
//        Meal newMeal2 = new Meal(11L,"PastaAndChicken", pastaAndChicken, MealDifficulty.MEDIUM);
//        mealService.save(newMeal2);
//
//        List<Ingredient>  milkAndStrawberry = Arrays.asList(milkIn,strawberryIn);
//        Meal newMeal3 = new Meal(12L,"MilkAndStrawberry", milkAndStrawberry, MealDifficulty.HARD);
//        mealService.save(newMeal3);
//
//        Meal newMeal4 = new Meal(13L, "OnlyBeef", Collections.singletonList(beefIn), MealDifficulty.INSANE);
//        mealService.save(newMeal4);
//    }

    @GetMapping("")
    public ResponseEntity<List<MealDTO>> findAllMeals(){
        return ResponseEntity.ok(mealFacade.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<MealDTO> findMealByName(@PathVariable String name){
        return ResponseEntity.ok(mealFacade.getByName(name));
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
