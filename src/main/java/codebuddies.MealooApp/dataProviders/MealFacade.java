package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.MealService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
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

    public MealDTO findMealByName(String name) throws ResourceNotFoundException {
        return  modelMapper.map(mealService.findByName(name), MealDTO.class);
        // todo add total macro and calories
    }

    public List<MealDTO> findAllMeals(Pageable pageable){
        List<MealDTO> mealsList = mealService.findAllPageable(pageable).stream()
                .map(meal -> modelMapper.map(meal, MealDTO.class)).collect(Collectors.toList());
        return  mealsList;
    }
}
