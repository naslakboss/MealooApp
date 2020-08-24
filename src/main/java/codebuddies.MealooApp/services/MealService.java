package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import codebuddies.MealooApp.repositories.IngredientRepository;
import codebuddies.MealooApp.repositories.MealRepository;
import codebuddies.MealooApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MealService {

    private ProductRepository productRepository;

    private MealRepository mealRepository;

    private IngredientRepository ingredientRepository;

    @Autowired
    public MealService(ProductRepository productRepository, MealRepository mealRepository, IngredientRepository ingredientRepository) {
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

    public Meal findByName(String name) {
        return mealRepository.findByName(name);
    }

    public List<Ingredient> changeIngredients(Meal meal){
        List<String> productsNames = meal.getIngredients().stream()
                .map(Ingredient::getProduct).map(Product::getName).collect(Collectors.toList());
        List<Integer> amounts = meal.getIngredients().stream()
                .map(Ingredient::getAmount).collect(Collectors.toList());
        List<Ingredient> ingredientList = new ArrayList<>();

        for(int i = 0; i < productsNames.size(); i ++){
            if (amounts.get(i) <= 0) {
                throw new IllegalStateException("Amount must be more than 0");
            }
            Product temporaryProduct = productRepository.findByName(productsNames.get(i));
            if(temporaryProduct == null){
                throw new IllegalStateException("Product: " + productsNames.get(i) + " doesn't " +
                        "exists in database");
            }
            Ingredient temporary = new Ingredient(amounts.get(i)
                    , temporaryProduct);
            ingredientList.add(temporary);
            ingredientRepository.save(temporary);
        }
        return ingredientList;
    }

    public Meal save(Meal meal) {
        List<Ingredient> ingredientList = changeIngredients(meal);
        Meal newMeal = new Meal(meal.getName(), ingredientList, meal.getMealDifficulty());
        return mealRepository.save(newMeal);
    }

    @Transactional
    public Meal updateByName(String name, Meal meal) {
        Meal foundedMeal = findByName(name);
        if(!foundedMeal.getFoodDiaries().isEmpty()){
            throw new IllegalStateException("This meal is used in the making of diaries." +
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
    public void deleteByName(String name) {
        if(!findByName(name).getFoodDiaries().isEmpty()){
            throw new IllegalStateException("This meal is used in the making of diaries." +
                    " You cannot delete or change it");
        }
        mealRepository.deleteByName(name);
    }
}
