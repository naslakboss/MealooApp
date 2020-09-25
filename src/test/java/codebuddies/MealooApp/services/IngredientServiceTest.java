package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.IngredientProvider;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class IngredientServiceTest {

    ModelMapper modelMapper;

    @Mock
    IngredientProvider ingredientProvider;

    @Mock
    ProductService productService;


    Product productEntity1;
    Product productEntity2;

    ProductDTO product1;

    Ingredient ingredient1;
    Ingredient ingredient2;

    List<Ingredient> ingredientList;

    Meal meal;

    IngredientService ingredientService;

    @BeforeEach
    void setUp(){
        modelMapper = new ModelMapper();

        productEntity1 = new Product("Carrot", 5.0
                ,new Macronutrients(1,10,0), 41, ProductType.VEGETABLES);
        productEntity2 = new Product("Apple", 6.0,
                new Macronutrients(0,14,0), 52, ProductType.FRUITS);

        product1 = modelMapper.map(productEntity1, ProductDTO.class);

        ingredient1 = new Ingredient(200, productEntity1);

        ingredient2 = new Ingredient(300, productEntity2);

        ingredientList = List.of(ingredient1, ingredient2);

        meal = new Meal("HealthyProduct", ingredientList, MealDifficulty.EASY);

        ingredientService = new IngredientService(ingredientProvider, productService);
    }

    @Test
    void shouldCreateIngredient(){
        //given
        given(ingredientProvider.createIngredient(product1, 200)).willReturn(ingredient1);
        //when
        Ingredient result = ingredientService.createIngredient(product1, 200);
        //then
        assertAll(
                () -> verify(ingredientProvider, times(1))
                        .createIngredient(product1, 200),
                () -> assertThat(result.getProduct().getName(), equalTo(product1.getName())),
                () -> assertThat(result.getAmount(), equalTo(200))
        );
    }

    @Test
    void shouldCreateIngredients(){
        //given
        given(ingredientProvider.createIngredient(product1, 200)).willReturn(ingredient1);
        given(productService.getProductByName("Carrot")).willReturn(product1);
        //when
        ingredientService.createIngredients(meal);
        //then
        verify(productService, times(2)).getProductByName(any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfIngredientAmountIsLessThanZero(){
        //given + when
        meal.getIngredients().get(0).setAmount(-200);
        //then
        assertThrows(IllegalArgumentException.class, () ->
                ingredientService.createIngredients(meal));
    }
}