package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.MealProvider;
import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class MealService {

    private ImageService imageService;

    private IngredientService ingredientService;

    private MealProvider mealProvider;

    @Autowired
    public MealService(MealProvider mealProvider, ImageService imageService, IngredientService ingredientService) {
        this.mealProvider = mealProvider;
        this.imageService = imageService;
        this.ingredientService = ingredientService;
    }


    public Page<MealDTO> getAllMeals(Pageable pageable){
        return mealProvider.getAllMeals(pageable);
    }

    public MealDTO getMealByName(String name){
        return mealProvider.getMealByName(name);
    }

    public MealDTO createMeal(Meal meal){
        ingredientService.createIngredients(meal);
        return mealProvider.createMeal(meal);
    }

    public MealDTO updateMealByName(MealDTO mealDTO, String name){
        MealDTO updatedMeal = getMealByName(name);
        updatedMeal.setName(mealDTO.getName());
        updatedMeal.setIngredients(mealDTO.getIngredients());
        updatedMeal.setPrice(mealDTO.getPrice());
        updatedMeal.setMealDifficulty(mealDTO.getMealDifficulty());
        updatedMeal.setMealMacronutrients(mealDTO.getMealMacronutrients());
        updatedMeal.setTotalCalories(mealDTO.getTotalCalories());
        updatedMeal.setRecipe(mealDTO.getRecipe());
        updatedMeal.setImages(mealDTO.getImages());
        return mealProvider.updateMeal(updatedMeal);
    }
    @Transactional
    public void deleteMealByName(String name){
        mealProvider.deleteByName(name);
    }

    public MealDTO addImageToMeal(String name, String filePath){
        MealDTO meal = getMealByName(name);
        imageService.createNewImage(meal, filePath);
        return meal;
    }

    public void deleteImageFromMeal(String name, String fileUrl){
        MealDTO meal = getMealByName(name);
        ImageDTO image = imageService.getImageByFileUrl(fileUrl);
        meal.getImages().remove(image);
    }

//
//    public List<String> findAllNamesOfMatchingMeals(int perfectCaloricValue){
//        return   findAll().stream()
//                .filter(meal -> meal.getTotalCalories() > perfectCaloricValue - 100 &&
//                    meal.getTotalCalories() < perfectCaloricValue + 100)
//                        .map(Meal::getName).collect(Collectors.toList());
//    }

}

