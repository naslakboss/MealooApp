package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.IllegalDataException;
import codebuddies.MealooApp.exceptions.MealIsNeededException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.IngredientRepository;
import codebuddies.MealooApp.repositories.MealRepository;
import codebuddies.MealooApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private ProductRepository productRepository;

    private MealRepository mealRepository;

    private IngredientRepository ingredientRepository;

    @Autowired
    public MealService(ProductRepository productRepository, MealRepository mealRepository
            , IngredientRepository ingredientRepository) {

        this.productRepository = productRepository;
        this.mealRepository = mealRepository;
        this.ingredientRepository = ingredientRepository;
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
            throw new ResourceNotFoundException("Meal of given name does not exist in database");
        }
        return meal;
    }

    public List<Ingredient> changeIngredients(Meal meal) throws ResourceNotFoundException {
        List<String> productsNames = meal.getIngredients().stream()
                .map(Ingredient::getProduct).map(Product::getName).collect(Collectors.toList());
        List<Integer> amounts = meal.getIngredients().stream()
                .map(Ingredient::getAmount).collect(Collectors.toList());
        List<Ingredient> ingredientList = new ArrayList<>();

        for(int i = 0; i < productsNames.size(); i ++){
            if (amounts.get(i) <= 0) {
                throw new IllegalDataException("Product amount must be more than 0");
            }
            Product temporaryProduct = productRepository.findByName(productsNames.get(i));
            if(temporaryProduct == null){
                throw new ResourceNotFoundException(productsNames.get(i) + " does not exist in database");
            }
            Ingredient temporary = new Ingredient(amounts.get(i)
                    , temporaryProduct);
            ingredientList.add(temporary);
            ingredientRepository.save(temporary);
        }
        return ingredientList;
    }

    public Meal save(Meal meal) throws ResourceNotFoundException {
        List<Ingredient> ingredientList = changeIngredients(meal);
        Meal newMeal = new Meal(meal.getName(), ingredientList, meal.getMealDifficulty());
        return mealRepository.save(newMeal);
    }

    @Transactional
    public Meal updateByName(String name, Meal meal) throws ResourceNotFoundException {
        Meal foundedMeal = findByName(name);
        if(!foundedMeal.getFoodDiaries().isEmpty()){
            throw new MealIsNeededException("This meal is used in the making of diaries." +
                    " You cannot delete or change it");
        }
        deleteByName(name);

        if(meal.getName() != null){
            foundedMeal.setName(meal.getName());
        }

        if(meal.getIngredients() != null){
            foundedMeal.setIngredients(changeIngredients(meal));
        }

        if(meal.getMealDifficulty()!=null){
            foundedMeal.setMealDifficulty(meal.getMealDifficulty());
        }
        foundedMeal.recalulateData();
        save(foundedMeal);
        return foundedMeal;
    }
    @Transactional
    public void deleteByName(String name) throws ResourceNotFoundException {
        if(!findByName(name).getFoodDiaries().isEmpty()){
            throw new MealIsNeededException("This meal is used in the making of diaries." +
                    " You cannot delete or change it");
        }
        mealRepository.deleteByName(name);
    }
}
