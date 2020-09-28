package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.MealMapper;
import codebuddies.MealooApp.dto.*;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private ImageService imageService;

    private IngredientService ingredientService;

    private MealMapper mealMapper;

    @Autowired
    public MealService(MealMapper mealMapper, ImageService imageService, IngredientService ingredientService) {
        this.mealMapper = mealMapper;
        this.imageService = imageService;
        this.ingredientService = ingredientService;
    }


    public Page<MealDTO> getAllMeals(Pageable pageable) {
        return mealMapper.getAllMeals(pageable);
    }

    public boolean existsByName(String name) {
        return mealMapper.existsByName(name);
    }

    public MealDTO getMealByName(String name) {
        return mealMapper.getMealByName(name);
    }

    public MealDTO createMeal(MealDTO meal) {
        if (existsByName(meal.getName())) {
            throw new EntityAlreadyFoundException(meal.getName());
        }
        ingredientService.createIngredients(meal);
        calculateData(meal);
        return mealMapper.createMeal(meal);
    }

    protected int calculateProteins(List<IngredientForMealDTO> ingredients) {
        int totalProteins = 0;
        for (int i = 0; i < ingredients.size(); i++) {
            totalProteins += ingredients.get(i).getProduct().getMacronutrients().getProteinsPer100g()
                    * ingredients.get(i).getAmount() / 100;
        }
        return totalProteins;
    }

    protected int calculateCarbohydrates(List<IngredientForMealDTO> ingredients) {
        int totalCarbohydrates = 0;
        for (int i = 0; i < ingredients.size(); i++) {
            totalCarbohydrates += ingredients.get(i).getProduct().getMacronutrients().getCarbohydratesPer100g()
                    * ingredients.get(i).getAmount() / 100;
        }
        return totalCarbohydrates;
    }

    protected int calculateFats(List<IngredientForMealDTO> ingredients) {
        int totalFats = 0;
        for (int i = 0; i < ingredients.size(); i++) {
            totalFats += ingredients.get(i).getProduct().getMacronutrients().getFatsPer100g()
                    * ingredients.get(i).getAmount() / 100;
        }
        return totalFats;
    }
    void calculateMealMacronutrients(MealDTO meal) {
        List<IngredientForMealDTO> ingredients = meal.getIngredients();
        meal.setMealMacronutrients(new MealMacronutrients
                (calculateProteins(ingredients), calculateCarbohydrates(ingredients), calculateFats(ingredients)));

    }

    public void calculatePrice(MealDTO meal) {
        List<IngredientForMealDTO> ingredients = meal.getIngredients();
        double totalPrice = 0;
        for (int i = 0; i < ingredients.size(); i++) {
            totalPrice += (ingredients.get(i).getProduct().getPrice() * ingredients.get(i).getAmount() / 1000);
        }
        meal.setPrice(totalPrice);
    }

    void calculateTotalCalories(MealDTO meal) {
        List<IngredientForMealDTO> ingredients = meal.getIngredients();
        int totalCalories = (calculateCarbohydrates(ingredients) * 4) + (calculateProteins(ingredients) * 4) + (calculateFats(ingredients) * 9);
        meal.setTotalCalories(totalCalories);
    }

    public void calculateData(MealDTO meal){
        calculateMealMacronutrients(meal);
        calculatePrice(meal);
        calculateTotalCalories(meal);
    }


    public MealDTO updateMealByName(MealDTO meal, String name) {
        meal.setName(name);
        ingredientService.createIngredients(meal);
        calculateData(meal);
        return mealMapper.updateMeal(meal);
    }

    @Transactional
    public void deleteMealByName(String name) {
        mealMapper.deleteByName(name);
    }

    public MealDTO addImageToMeal(String name, String filePath) {
        MealDTO meal = getMealByName(name);
        imageService.createNewImage(meal, filePath);
        return meal;
    }

    public void deleteImageFromMeal(String name, String fileUrl) {
        MealDTO meal = getMealByName(name);
        ImageDTO image = imageService.getImageByFileUrl(fileUrl);
        meal.getImages().remove(image);
    }

    public List<String> findNamesOfMatchingMeals(int perfectCaloricValue) {
        int lowerBorder = perfectCaloricValue - 100;
        int upperBorder = perfectCaloricValue + 100;
        return mealMapper.findNamesOfMatchingMeals(lowerBorder, upperBorder).stream()
                .map(MealDTO::getName).collect(Collectors.toList());
    }

}

