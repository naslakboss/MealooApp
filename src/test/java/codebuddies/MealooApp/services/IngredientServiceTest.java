package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.IngredientProvider;
import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
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

    @Mock
    IngredientProvider ingredientProvider;

    @Mock
    ProductService productService;

    ProductForIngredientDTO product1;
    ProductForIngredientDTO product2;

    IngredientForMealDTO ingredient1;
    IngredientForMealDTO ingredient2;

    List<IngredientForMealDTO> ingredientList;

    MealDTO meal;

    IngredientService ingredientService;

    @BeforeEach
    void setUp() {

        product1 = new ProductForIngredientDTO("Carrot", 5.0, 41);
        product2 = new ProductForIngredientDTO("Apple", 6.0, 56);

        ingredient1 = new IngredientForMealDTO(product1, 200);
        ingredient2 = new IngredientForMealDTO(product2, 300);

        ingredientList = new ArrayList<>();
        ingredientList.add(ingredient1);
        ingredientList.add(ingredient2);

        meal = new MealDTO("HealthyProduct", ingredientList, 2.2, MealDifficulty.EASY
                , new MealMacronutrients(2, 40, 1), 192, "Recipe", Collections.emptyList());

        ingredientService = new IngredientService(ingredientProvider, productService);
    }

    @Test
    void shouldCreateIngredient() {
        //given
        given(ingredientProvider.createIngredient(product1, 200)).willReturn(ingredient1);

        //when
        IngredientForMealDTO result = ingredientService.createIngredient(product1, 200);

        //then
        assertAll(
                () -> verify(ingredientProvider, times(1))
                        .createIngredient(product1, 200),
                () -> assertThat(result.getProduct().getName(), equalTo(product1.getName())),
                () -> assertThat(result.getAmount(), equalTo(200))
        );
    }

    @Test
    void shouldCreateIngredients() {

        //given
        given(productService.getProductForIngredientByName("Carrot")).willReturn(product1);
        given(productService.getProductForIngredientByName("Apple")).willReturn(product2);

        //when
        ingredientService.createIngredients(meal);

        //then
        verify(productService, times(2)).getProductForIngredientByName(any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfIngredientAmountIsLessThanZero() {
        //given + when
        meal.getIngredients().get(0).setAmount(-200);

        //then
        assertThrows(IllegalArgumentException.class, () ->
                ingredientService.createIngredients(meal));
    }
}