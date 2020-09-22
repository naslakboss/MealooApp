package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.MealRepository;
import codebuddies.MealooApp.services.MealService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealProvider {

    ModelMapper modelMapper = new ModelMapper();

    MealRepository mealRepository;

    public MealProvider(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Page<MealDTO> getAllMeals(Pageable pageable){
        return mealRepository.findAll(pageable)
                .map(meal -> modelMapper.map(meal, MealDTO.class));
    }

    public MealDTO getMealByName(String name){
        Meal meal =  mealRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Meal " + name + " does not exists"));
        return modelMapper.map(meal, MealDTO.class);
    }

    public MealDTO createMeal(Meal meal){
        Meal newMeal = mealRepository.save(new Meal
                (meal.getName(),meal.getIngredients()
                        , meal.getMealDifficulty(), meal.getRecipe()));
        return modelMapper.map(newMeal, MealDTO.class);
    }

    public MealDTO updateMeal(MealDTO updatedMeal){
        Meal meal = modelMapper.map(updatedMeal, Meal.class);
        mealRepository.save(meal);
        return updatedMeal;
    }

    public boolean existsByName(String name){
        return mealRepository.existsByName(name);
    }

    public void deleteByName(String name){
        if(!existsByName(name)){
            throw new ResourceNotFoundException("Meal " + name + " does not exist in database");
        }
        mealRepository.deleteByName(name);
    }
}
