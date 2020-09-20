package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.repositories.IngredientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientForMealProvider {

    private IngredientRepository ingredientRepository;

    private ModelMapper modelMapper;

    public IngredientForMealProvider(IngredientRepository ingredientRepository, ModelMapper modelMapper) {
        this.ingredientRepository = ingredientRepository;
        this.modelMapper = modelMapper;
    }

    public IngredientForMealDTO getIngredientById(long id){
        return modelMapper.map(ingredientRepository.findById(id), IngredientForMealDTO.class);
    }

    public List<IngredientForMealDTO> getAllIngredients(){
        List<IngredientForMealDTO> listOfIngredients = ingredientRepository.findAll()
                .stream().map(ingredient -> modelMapper.map(ingredient, IngredientForMealDTO.class))
                .collect(Collectors.toList());
        return listOfIngredients;

    }
}
