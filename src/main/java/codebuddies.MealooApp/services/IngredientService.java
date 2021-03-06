package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.IngredientMapper;
import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientMapper ingredientMapper;

    private final ProductService productService;

    public IngredientService(IngredientMapper ingredientMapper, ProductService productService) {
        this.ingredientMapper = ingredientMapper;
        this.productService = productService;
    }

    public IngredientForMealDTO createIngredient(ProductForIngredientDTO product, Integer productAmount) {
        return ingredientMapper.createIngredient(product, productAmount);
    }

    public void createIngredients(MealDTO meal) {

        List<String> productNames = meal.getIngredients().stream()
                .map(IngredientForMealDTO::getProduct)
                .map(ProductForIngredientDTO::getName)
                .collect(Collectors.toList());

        List<Integer> amounts = meal.getIngredients().stream()
                .map(IngredientForMealDTO::getAmount)
                .collect(Collectors.toList());

        List<IngredientForMealDTO> ingredients = new ArrayList<>();

        for (int i = 0; i < productNames.size(); i++) {
            if (amounts.get(i) < 1) {
                throw new IllegalArgumentException("Amount of product must be greater than 0");
            }
            ingredients.add(createIngredient(productService.getProductForIngredientByName(productNames.get(i)), amounts.get(i)));
        }
        meal.setIngredients(ingredients);
    }
}
