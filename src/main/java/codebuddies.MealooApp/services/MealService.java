package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.IllegalDataException;
import codebuddies.MealooApp.exceptions.MealIsNeededException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.IngredientRepository;
import codebuddies.MealooApp.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealService {

    private ProductService productService;

    private MealRepository mealRepository;

    private IngredientRepository ingredientRepository;

    private ImageService imageService;

    @Autowired
    public MealService(ProductService productService, MealRepository mealRepository
            , IngredientRepository ingredientRepository, ImageService imageService) {

        this.productService = productService;
        this.mealRepository = mealRepository;
        this.ingredientRepository = ingredientRepository;
        this.imageService = imageService;
    }

    public List<Meal> findAll() {
        return mealRepository.findAll();
    }

    public boolean existsByName(String name) {
        return mealRepository.existsByName(name);
    }

    public Meal findByName(String name) throws ResourceNotFoundException {
        Meal meal = mealRepository.findByName(name);
        if(meal == null){
            throw new ResourceNotFoundException(name);
        }
        return meal;
    }

    public List<Ingredient> createListOfIngredients(Meal meal) throws ResourceNotFoundException {
        List<String> productsNames = meal.getIngredients().stream()
                .map(Ingredient::getProduct).map(Product::getName).collect(Collectors.toList());
        List<Integer> amounts = meal.getIngredients().stream()
                .map(Ingredient::getAmount).collect(Collectors.toList());
        List<Ingredient> ingredientList = new ArrayList<>();

        for(int i = 0; i < productsNames.size(); i ++){
            if (amounts.get(i) <= 0) {
                throw new IllegalDataException("Product amount must be more than 0");
            }
            Product temporaryProduct = productService.findByName(productsNames.get(i));
            Ingredient temporary = new Ingredient(amounts.get(i)
                    , temporaryProduct);
            ingredientList.add(temporary);
            ingredientRepository.save(temporary);
        }
        return ingredientList;
    }

    public Meal save(Meal meal) throws ResourceNotFoundException {
        List<Ingredient> ingredientList = createListOfIngredients(meal);
        Meal newMeal = new Meal(meal.getName(), ingredientList, meal.getMealDifficulty());
        return mealRepository.save(newMeal);
    }

    @Transactional
    public Meal updateByName(String name, Meal meal) throws ResourceNotFoundException {
        Meal foundedMeal = findByName(name);

        if(!foundedMeal.getFoodDiaries().isEmpty()){
            throw new MealIsNeededException();
        }
        if(meal.getName() != null){
            foundedMeal.setName(meal.getName());
        }

        if(meal.getIngredients() != null){
            foundedMeal.setIngredients(createListOfIngredients(meal));
        }

        if(meal.getMealDifficulty()!=null){
            foundedMeal.setMealDifficulty(meal.getMealDifficulty());
        }
        if(meal.getRecipe()!=null){
            foundedMeal.setRecipe(meal.getRecipe());
        }
        foundedMeal.recalculateData();
        save(foundedMeal);
        return foundedMeal;
    }
    @Transactional
    public void deleteByName(String name) throws ResourceNotFoundException {
        if(!findByName(name).getFoodDiaries().isEmpty()){
            throw new MealIsNeededException();
        }
        if(mealRepository.findByName(name) == null){
            throw new ResourceNotFoundException("This meal does not exist in repository");
        }
        mealRepository.deleteByName(name);
    }

    public void addImageToMeal(String name, String filePath) throws IOException {
        Meal meal = findByName(name);
        Map result  =  imageService.addNewImage(filePath);
        String imageUrl = result.get("url").toString();
        String publicId = result.get("public_id").toString();
        Image newImg = new Image(filePath, imageUrl, meal);
        imageService.save(newImg);
    }

    public void deleteImageFromMeal(String name, String fileUrl) throws IOException {
        Meal meal = findByName(name);
        Optional<String> imageUrl = meal.getImages().stream().map(Image::getFileUrl).findAny();
        if(imageUrl.isEmpty()){
            throw new ResourceNotFoundException("Image with given publicID is not attached to this meal");
        }
        imageService.deleteByFileUrl(imageUrl.get());
    }

    public List<String> findAllNamesOfMatchingMeals(int perfectCaloricValue){
        return   findAll().stream()
                .filter(meal -> meal.getTotalCalories() > perfectCaloricValue - 100 &&
                    meal.getTotalCalories() < perfectCaloricValue + 100)
                        .map(Meal::getName).collect(Collectors.toList());
    }

    public List<String> findAllNamesOfMealsForCorrectDeficit(int deficit){
        return   findAll().stream()
                .filter(meal -> meal.getTotalCalories() > deficit - 100 &&
                    meal.getTotalCalories() < deficit + 100)
                        .map(Meal::getName).collect(Collectors.toList());
    }
}

