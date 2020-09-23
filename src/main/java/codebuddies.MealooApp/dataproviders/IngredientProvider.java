package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.repositories.IngredientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class IngredientProvider {

    ModelMapper modelMapper = new ModelMapper();

    IngredientRepository ingredientRepository;

    public IngredientProvider(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient createIngredient(ProductDTO productDTO, Integer amountOfProduct){
        Product product = modelMapper.map(productDTO, Product.class);
        return new Ingredient(amountOfProduct, product);
    }
}
