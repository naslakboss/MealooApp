package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.IngredientProvider;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    IngredientProvider ingredientProvider;

    ProductService productService;

    public IngredientService(IngredientProvider ingredientProvider, ProductService productService) {
        this.ingredientProvider = ingredientProvider;
        this.productService = productService;
    }

    public Ingredient createIngredient(ProductDTO product, Integer productAmount){
        return ingredientProvider.createIngredient(product, productAmount);
    }

    public void createIngredients(Meal meal) {
        List<String> productNames = meal.getIngredients().stream()
                .map(Ingredient::getProduct)
                .map(Product::getName)
                .collect(Collectors.toList());

        List<Integer> amounts = meal.getIngredients().stream()
                .map(Ingredient::getAmount)
                .collect(Collectors.toList());

        List<Ingredient> ingredients = new ArrayList<>();

        for(int i = 0; i < productNames.size(); i++){
            if(amounts.get(i) < 1){
                throw new IllegalArgumentException("Amount of product must be greater than 0");
            }
            ingredients.add(createIngredient(productService.getProductByName(productNames.get(i)), amounts.get(i)));
        }
        meal.setIngredients(ingredients);

    }
}
