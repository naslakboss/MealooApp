package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dataproviders.MealProvider;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.IngredientRepository;
import codebuddies.MealooApp.repositories.MealRepository;
import codebuddies.MealooApp.services.MealService;
import codebuddies.MealooApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/meals")
public class MealController {

    private MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void testNewMeal(){
//
//        Product bread = productService.findByName("Bread");
//        Product egg = productService.findByName("Eggs");
//        Product milk = productService.findByName("Milk");
//        Product beef = productService.findByName("Beef");
//        Product chicken = productService.findByName("Chicken");
//        Product pasta = productService.findByName("Pasta");
//        Product strawberry = productService.findByName("Strawberry");
//        Product chickenBreast = productService.findByName("ChickenBreast");
//        Product rice = productService.findByName("WhiteRice");
//        Product paprika = productService.findByName("Paprika");
//
//        Ingredient breadI = new Ingredient(300, bread);
//        Ingredient milkI = new Ingredient(500, milk);
//        Ingredient beefI = new Ingredient(200, beef);
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
//        Meal newMeal2 = new Meal("PastaAndChicken", pastaAndChicken, MealDifficulty.MEDIUM);
//        mealService.save(newMeal2);
//
//        List<Ingredient>  milkAndStrawberry = Arrays.asList(milkIn,strawberryIn);
//        Meal newMeal3 = new Meal("MilkAndStrawberry", milkAndStrawberry, MealDifficulty.HARD);
//        mealService.save(newMeal3);
//
//        Meal newMeal4 = new Meal("OnlyBeef", Collections.singletonList(beefIn), MealDifficulty.INSANE);
//        mealService.save(newMeal4);
//    }


    @GetMapping("")
    public ResponseEntity<Page<MealDTO>> getAllMeals(Pageable pageable){
        return ResponseEntity.ok(mealService.getAllMeals(pageable));
    }

    @GetMapping("/{name}")
    public ResponseEntity<MealDTO> getMeal(@PathVariable String name) {
        return ResponseEntity.ok(mealService.getMealByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<MealDTO> createMeal(@Valid @RequestBody Meal meal){
        return ResponseEntity.ok(mealService.createMeal(meal));
    }

    @PostMapping("{name}/image")
    public ResponseEntity<MealDTO> addImage(@PathVariable String name, @RequestParam("filePath") String filePath) throws IOException {
        return ResponseEntity.ok(mealService.addImageToMeal(name, filePath));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/{name}/image")
    public ResponseEntity deleteImage(@PathVariable String name, @RequestParam("fileUrl") String fileUrl) throws IOException {
        mealService.deleteImageFromMeal(name, fileUrl);
        return ResponseEntity.ok("Image was successfully deleted from the meal");
    }

    @PutMapping("/{name}")
    public ResponseEntity<MealDTO> updateMeal(@Valid @RequestBody MealDTO mealDTO, @PathVariable String name) {
        return ResponseEntity.ok(mealService.updateMealByName(mealDTO, name));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/{name}")
    public ResponseEntity deleteMeal(@PathVariable String name) {
        mealService.deleteMealByName(name);
        return ResponseEntity.ok("Meal " + name + " was successfully deleted from Repository");
    }
}
