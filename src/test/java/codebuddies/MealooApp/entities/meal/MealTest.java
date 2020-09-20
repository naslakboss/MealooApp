package codebuddies.MealooApp.entities.meal;

import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class MealTest {

    Product product;
    Product product2;
    Ingredient ingredient1;
    Ingredient ingredient2;
    List<Ingredient> listOfIngredients;
    Meal meal;

    @BeforeEach
    void setUp(){
//
//        product = new Product("Potato", 5, new Macronutrients(2, 17,0), ProductType.GRAINS);
//        product2 = new Product("Beef", 30, new Macronutrients(26, 0, 15), ProductType.MEAT);

        ingredient1 = new Ingredient(300, product);
        ingredient2 = new Ingredient(200, product2);

        listOfIngredients = Arrays.asList(ingredient1, ingredient2);

        meal = new Meal("BeefAndPotato", listOfIngredients, MealDifficulty.HARD);
    }

    @Test
    void creatingMealWithProperIngredientShouldCalculatePriceAutomaticallyAndCorrect() {
        //given
        //when
        // 5 * 0,3 + 30 * 0,2 = 7.5
        //then
        assertThat(meal.getPrice(), equalTo(7.5));
    }

    @Test
    void creatingMealWithProperMacronutrientsShouldCalculateTotalProteinsAutomaticallyAndCorrect(){
        //given
        int amount1 = meal.getIngredients().get(0).getAmount();
        int proteinsIn100gOfProduct1 = meal.getIngredients().get(0).getProduct().getMacronutrients().getProteinsPer100g();

        int amount2 = meal.getIngredients().get(1).getAmount();
        int proteinsIn100gOfProduct2 = meal.getIngredients().get(1).getProduct().getMacronutrients().getProteinsPer100g();
        //when
        int result = (amount1/100 * proteinsIn100gOfProduct1) + (amount2/100 * proteinsIn100gOfProduct2);
        //then
        assertThat(meal.calculareProteins(), equalTo(result));
    }

    @Test
    void creatingMealWithProperMacronutrientsShouldCalculateTotalCarbohydratesAutomaticallyAndCorrect(){
        //given
        int amount1 = meal.getIngredients().get(0).getAmount();
        int carbohydratesIn100gOfProduct1 = meal.getIngredients().get(0).getProduct().getMacronutrients().getCarbohydratesPer100g();

        int amount2 = meal.getIngredients().get(1).getAmount();
        int carbohydratesIn100gOfProduct2 = meal.getIngredients().get(1).getProduct().getMacronutrients().getCarbohydratesPer100g();
        //when
        int result = (amount1/100 * carbohydratesIn100gOfProduct1) + (amount2/100 * carbohydratesIn100gOfProduct2);
        //then
        assertThat(meal.calculateCarbohydrates(), equalTo(result));
    }

    @Test
    void creatingMealWithProperMacronutrientsShouldCalculateTotalFatsAutomaticallyAndCorrect(){
        //given
        int amount1 = meal.getIngredients().get(0).getAmount();
        int fatsIn100gOfProduct1 = meal.getIngredients().get(0).getProduct().getMacronutrients().getFatsPer100g();

        int amount2 = meal.getIngredients().get(1).getAmount();
        int fatsIn100gOfProduct2 = meal.getIngredients().get(1).getProduct().getMacronutrients().getFatsPer100g();
        //when
        int result = (amount1/100 * fatsIn100gOfProduct1) + (amount2/100 * fatsIn100gOfProduct2);
        //then
        assertThat(meal.calculateFats(), equalTo(result));

    }

    @Test
    void creatingMealWithProperMacronutrientsShouldReturnCorrectValuesOfAllComponents(){
        //given
        //when
        //then
        assertAll(
                () -> assertThat(meal.getMealMacronutrients().getTotalProteins(), equalTo(meal.calculareProteins())),
                () -> assertThat(meal.getMealMacronutrients().getTotalCarbohydrates(), equalTo(meal.calculateCarbohydrates())),
                () -> assertThat(meal.getMealMacronutrients().getTotalFats(), equalTo(meal.calculateFats()))
        );
    }

    @Test
    void creatingMealWithProperMacronutrientsShouldCalculateTotalCaloriesAutomaticallyAndCorrect() {
        assertThat(meal.getTotalCalories(), equalTo(meal.calculateCalories()));
    }

    @Test
    void changesInMealComponentsShouldRecalculateData() {
        //given
        int oldCalories = meal.getTotalCalories();
        int oldTotalProteins = meal.getMealMacronutrients().getTotalProteins();
        double oldTotalPrice = meal.getPrice();
        int oldFats = meal.getMealMacronutrients().getTotalFats(); // Because product doesn't contain fats
        meal.getIngredients().get(0).setAmount(700);
        //when
        meal.recalculateData();
        //then
        assertAll(
                () -> assertThat(meal.getTotalCalories(), not(lessThan(oldCalories))),
                () -> assertThat(meal.getMealMacronutrients().getTotalProteins(), not(lessThan(oldTotalProteins))),
                () -> assertThat(oldTotalPrice, lessThan(meal.getPrice())),
                () -> assertThat(meal.getMealMacronutrients().getTotalFats(), equalTo(oldFats))
        );
    }
}