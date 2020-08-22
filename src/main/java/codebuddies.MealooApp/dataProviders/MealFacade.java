package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.services.MealService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealFacade {

    ModelMapper modelMapper;

    MealService mealService;

    public MealFacade(ModelMapper modelMapper, MealService mealService) {
        this.modelMapper = modelMapper;
        this.mealService = mealService;
    }

    public MealDTO findMealByName(String name){
        return  modelMapper.map(mealService.findByName(name), MealDTO.class);
        // todo add total macro and calories
    }

    public List<MealDTO> findAllMeals(){
        List<MealDTO> mealsList = mealService.findAll().stream()
                .map(meal -> modelMapper.map(meal, MealDTO.class)).collect(Collectors.toList());
        return  mealsList;
    }
}
