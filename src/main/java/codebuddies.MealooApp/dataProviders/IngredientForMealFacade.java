package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.repositories.IngredientRepository;
import codebuddies.MealooApp.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientForMealFacade {

    private IngredientRepository ingredientRepository;

    private ModelMapper modelMapper;

    public IngredientForMealFacade(IngredientRepository ingredientRepository, ModelMapper modelMapper) {
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
