package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.repositories.IngredientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientProvider {

    ModelMapper modelMapper;

    IngredientRepository ingredientRepository;

    public IngredientProvider(ModelMapper modelMapper, IngredientRepository ingredientRepository) {
        this.modelMapper = modelMapper;
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient createIngredient(ProductDTO productDTO, Integer amountOfProduct){
        Product product = modelMapper.map(productDTO, Product.class);
        return new Ingredient(amountOfProduct, product);
    }
}
