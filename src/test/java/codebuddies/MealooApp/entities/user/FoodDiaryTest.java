package codebuddies.MealooApp.entities.user;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class FoodDiaryTest {

    Product product1;
    Product product2;
    Product product3;

    Ingredient ingredient1;
    Ingredient ingredient2;
    Ingredient ingredient3;

    List<Ingredient> listOfIngredients1;
    List<Ingredient> listOfIngredients2;
    List<Ingredient> listOfIngredients3;

    Meal meal1;
    Meal meal2;
    Meal meal3;
    List<Meal> listOfMeals;
    MealooUser mealooUser;
    FoodDiary foodDiary;

    @BeforeEach
    void setUp(){
        product1 = new Product("Rice", 5, new Macronutrients(7, 79, 1), ProductType.GRAINS);
        product2 = new Product("Chicken", 12, new Macronutrients(22, 1, 4), ProductType.MEAT);
        product3 = new Product("Strawberry", 8, new Macronutrients(1, 8, 0), ProductType.GRAINS);

        ingredient1 = new Ingredient(100, product1);
        ingredient2 = new Ingredient(200, product2);
        ingredient3 = new Ingredient(500, product3);

        listOfIngredients1 = Arrays.asList(ingredient1, ingredient2);
        listOfIngredients2 = Arrays.asList(ingredient1, ingredient3);
        listOfIngredients3 = Arrays.asList(ingredient2, ingredient3);

        meal1 = new Meal("RiceAndChicken", listOfIngredients1, MealDifficulty.MEDIUM);
        meal2 = new Meal("RiceAndStrawberry", listOfIngredients2, MealDifficulty.EASY);
        meal3 = new Meal("ChickenAndStrawberry", listOfIngredients3, MealDifficulty.INSANE);

        listOfMeals = new ArrayList<>();
        listOfMeals.add(meal1);
        listOfMeals.add(meal2);

        mealooUser = new MealooUser("User", "secretPassword", "johnsmith@gmail.com"
                , new NutritionSettings(3000));
        foodDiary = new FoodDiary(listOfMeals, LocalDate.now(), mealooUser);
    }

    @Test
    void addMealToListShouldIncrementSizeOfList() {
        //given
        //when
        listOfMeals.add(meal3);
        //then
        assertAll(
                () -> assertThat(listOfMeals.size(), equalTo(3)),
                () -> assertThat(listOfMeals.get(2), sameInstance(meal3))
        );
    }

    @Test
    void deleteMealFromListShouldDecrementSizeOfList() {
        //given
        //when
        listOfMeals.remove(meal1);
        //then
        assertAll(
                () -> assertThat(listOfMeals.size(),equalTo(1)),
                () -> assertThat(listOfMeals.get(0), equalTo(meal2))
        );
    }

    @Test
    void shouldCalculatePriceCorrectlyFromAllMealInDiary() {
        //given
        float mealPrice1 = (float) meal1.getPrice();
        float mealPrice2 = (float) meal2.getPrice();
        float totalprice = mealPrice1 + mealPrice2;
        //when
        foodDiary.calculatePrice();
        //then
        assertAll(
                () -> assertThat(foodDiary.getTotalPrice(), equalTo(totalprice)),
                () -> assertThat(foodDiary.getTotalPrice(), greaterThanOrEqualTo(mealPrice1)),
                () -> assertThat(foodDiary.getTotalPrice(), greaterThanOrEqualTo(mealPrice2))
        );
    }

    @Test
    void shouldCalculateCaloriesCorrectlyFromAllMealsInDiary() {
        //given
        int caloriesFromMeal1 = meal1.getTotalCalories();
        int caloriesFromMeal2 = meal1.getTotalCalories();
        int totalCalories = caloriesFromMeal1 + caloriesFromMeal2;
        //when
        foodDiary.calculateCalories();
        //then
        assertThat(foodDiary.getTotalCalories(), equalTo(totalCalories));
    }

    @Test
    void shouldCalculateMacronutrientsFromAllMealsInDiary() {
        //given
        int proteinsFromMeal1 = meal1.getMealMacronutrients().getTotalProteins();
        int carbohydratesFromMeal1 = meal1.getMealMacronutrients().getTotalCarbohydrates();
        int fatsFromMeal1 = meal1.getMealMacronutrients().getTotalFats();

        int proteinsFromMeal2 = meal2.getMealMacronutrients().getTotalProteins();
        int carbohydratesFromMeal2 = meal2.getMealMacronutrients().getTotalCarbohydrates();
        int fatsFromMeal2 = meal2.getMealMacronutrients().getTotalFats();

        int totalProteins =proteinsFromMeal1 + proteinsFromMeal2;
        int totalCarbohydrates =carbohydratesFromMeal1 + carbohydratesFromMeal2;
        int totalFats =fatsFromMeal1 + fatsFromMeal2;
        //when
        foodDiary.calculateMacronutrients();
        //then
    }
}