package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.NutritionSettings;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class FoodDiaryServiceTest {

    @Mock
    FoodDiaryRepository foodDiaryRepository;

    @Mock
    MealService mealService;

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

    List<Meal> listOfMeals1;
    List<Meal> listOfMeals2;
    List<Meal> listOfMeals3;
    MealooUser mealooUser1;
    MealooUser mealooUser2;
    FoodDiary foodDiary1;
    FoodDiary foodDiary2;
    FoodDiary foodDiary3;
    List<FoodDiary> listOfDiaries;

    FoodDiaryService foodDiaryService;

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

        listOfMeals1 = new ArrayList<>();
        listOfMeals1.add(meal1);
        listOfMeals1.add(meal2);

        listOfMeals2 = new ArrayList<>(List.of(meal2, meal3));
        listOfMeals3 = new ArrayList<>();
        listOfMeals3.add(meal1);
        listOfMeals3.add(meal3);

        mealooUser1 = new MealooUser("User", "secretPassword", "johnsmith@gmail.com"
                , new NutritionSettings(3000));
        mealooUser2 = new MealooUser("Admin", "secretHardPassword", "andrewsmith@gmail.com"
                , new NutritionSettings(3200));
        foodDiary1 = new FoodDiary(listOfMeals1, LocalDate.now(), mealooUser1);
        foodDiary2 = new FoodDiary(listOfMeals2, LocalDate.now(), mealooUser1);
        foodDiary3 = new FoodDiary(listOfMeals3, LocalDate.now(), mealooUser2);
        listOfDiaries = new ArrayList<>(List.of(foodDiary1, foodDiary2, foodDiary3));

        foodDiaryService = new FoodDiaryService(foodDiaryRepository, mealService);
    }

    @Test
    void shouldSaveFoodDiary() {
        //given
        given(foodDiaryRepository.save(foodDiary1)).willReturn(foodDiary1);
        //when
        FoodDiary result = foodDiaryService.save(foodDiary1);
        //then
        assertThat(result, sameInstance(foodDiary1));
    }

    @Test
    void shouldFindAllDiariesForOneUser() {
        //given
        List<FoodDiary> listOfDiaries = new ArrayList<>();
        listOfDiaries.add(foodDiary1);
        listOfDiaries.add(foodDiary2);
        given(foodDiaryRepository.findAll()).willReturn(listOfDiaries);
        //when
        List<FoodDiary> diaries = foodDiaryService.findAll();
        //then
        assertAll(
                () -> assertThat(diaries.get(0), sameInstance(foodDiary1)),
                () -> assertThat(diaries.get(1), sameInstance(foodDiary2)),
                () -> assertThat(diaries.size(), sameInstance(2))
        );
    }

    @Test
    void shouldFindAllDiariesIfFoodDiariesWithGivenDataExist() {
        //given
        List<FoodDiary> listOfDiaries = new ArrayList<>();
        listOfDiaries.add(foodDiary1);
        listOfDiaries.add(foodDiary2);
        listOfDiaries.add(foodDiary3);

        given(foodDiaryRepository.findByDate(LocalDate.now())).willReturn(Optional.of(listOfDiaries));
        //when
        List<FoodDiary> result = foodDiaryService.findByDate(LocalDate.now());
        //then
        assertThat(result, sameInstance(listOfDiaries));
    }

    @Test
    void shouldThrowAResourceNotFoundExceptionWhenFoodDiariesWithGivenDataDoesNotExist() {
        //given + when
        given(foodDiaryRepository.findByDate(LocalDate.now())).willThrow(ResourceNotFoundException.class);
        //then
        assertThrows(ResourceNotFoundException.class, () -> foodDiaryService.findByDate(LocalDate.now()));
    }

    @Test
    void shouldReturnAllDiariesForOnlyOneUser() {
        //given
        List<FoodDiary> listOfDiaries2 = new ArrayList<>();
        listOfDiaries2.add(foodDiary1);
        listOfDiaries2.add(foodDiary2);
        listOfDiaries2.add(foodDiary3);
        given(foodDiaryRepository.findAll()).willReturn(listOfDiaries2);
        //when
        List<FoodDiary> foodDiaries = foodDiaryService.findAllDiaries(mealooUser1);
        //then
        assertAll(
                () -> assertThat(foodDiaries.size(), equalTo(2)),
                () -> assertThat(foodDiaries.get(0), not(equalTo(foodDiary3))),
                () -> assertThat(foodDiaries.get(1), not(equalTo(foodDiary3)))
        );
    }

    @Test
    void shouldCreateNewDiaryForCurrentDate() {
        //given
        MealMacronutrients empty = new MealMacronutrients(0,0,0);
        //
        FoodDiary result = foodDiaryService.createNewDiary(mealooUser1);
        //then
        assertAll(
                () -> assertThat(result.getDate(), equalTo(LocalDate.now())),
                () -> assertThat(result.getTotalCalories(), equalTo(0)),
                () -> assertThat(result.getTotalPrice(), equalTo(0.0F)),
                () -> assertThat(result.getMealMacronutrients(), equalTo(empty)),
                () -> verify(foodDiaryRepository, times(1)).save(any())

        );
    }

    @Test
    void shouldFindDairyOfCurrentDateIfExistForGivenUser(){
        //given
        LocalDate date = LocalDate.of(2020, 8, 25);
        foodDiary2.setDate(date);
        List<FoodDiary> diaries = new ArrayList<>();
        diaries.add(foodDiary1);
        diaries.add(foodDiary2);
        given(foodDiaryRepository.findAll()).willReturn(diaries);
        //when
        FoodDiary result = foodDiaryService.findDiaryOfDay(mealooUser1, date.toString());
        //then
        assertThat(result, sameInstance(foodDiary2));
    }

    @Test
    void shouldCreateNewDiaryForCurrentDiaryIfNotExistForGivenUser(){
        //given
        LocalDate date = LocalDate.of(2020, 8, 25);;
        List<FoodDiary> diaries = new ArrayList<>();
        diaries.add(foodDiary1);
        diaries.add(foodDiary2);
        given(foodDiaryRepository.findAll()).willReturn(diaries);
        FoodDiary newDiary = new FoodDiary(Collections.emptyList(), LocalDate.now(), mealooUser1);
        //when
        FoodDiary result = foodDiaryService.findDiaryOfDay(mealooUser1, date.toString());
        //then
        assertAll(
                () -> assertThat(result.getDate(), equalTo(newDiary.getDate())),
                () -> assertThat(result.getTotalPrice(), equalTo(0.0F)),
                () -> assertThat(result.getTotalCalories(), equalTo(0)),
                () -> assertThat(result.getListOfMeals(), equalTo(Collections.emptyList())),
                () -> assertThat(result.getMealMacronutrients().getTotalCarbohydrates(), equalTo(0))
        );
    }

    @Test
    void shouldAddMealToCurrentDiaryIfMealExist() {
        //given
        given(mealService.findByName("RiceAndStrawberry")).willReturn(meal2);
        List<FoodDiary> listOfDiaries = new ArrayList<>();
        listOfDiaries.add(foodDiary1);
        listOfDiaries.add(foodDiary2);
        listOfDiaries.add(foodDiary3);
        int totalCaloriesBefore = foodDiary3.getTotalCalories();
        float totalPriceBefore = foodDiary3.getTotalPrice();
        given(foodDiaryRepository.findAll()).willReturn(listOfDiaries);
        mealooUser2.setFoodDiaries(List.of(foodDiary3));
        //when
        FoodDiary result = foodDiaryService.addMealToCurrentDiary(mealooUser2, "RiceAndStrawberry");
        //then
        assertAll(
                () -> assertThat(result.getListOfMeals().size(), equalTo(3)),
                () -> assertThat(result.getListOfMeals().get(2).getName(), equalTo("RiceAndStrawberry")),
                () -> assertThat(result.getTotalCalories(), greaterThan(totalCaloriesBefore)),
                () -> assertThat(result.getTotalPrice(), greaterThan(totalPriceBefore))
        );
    }

    @Test
    void shouldDeleteMealFromCurrentDiaryIfMealExist() {
        //given
        given(mealService.findByName("RiceAndChicken")).willReturn(meal1);
        int totalCaloriesBefore = foodDiary3.getTotalCalories();
        float totalPriceBefore = foodDiary3.getTotalPrice();
        given(foodDiaryRepository.findAll()).willReturn(listOfDiaries);
        mealooUser2.setFoodDiaries(List.of(foodDiary3));
        //when
        FoodDiary result = foodDiaryService.deleteMealFromCurrentDiary(mealooUser2, "RiceAndChicken");
        //then
        assertAll(
                () -> assertThat(result.getListOfMeals().size(), equalTo(1)),
                () -> assertThat(result.getListOfMeals(), not(Matchers.contains(meal1))),
                () -> assertThat(result.getTotalCalories(), lessThan(totalCaloriesBefore)),
                () -> assertThat(result.getTotalPrice(), lessThan(totalPriceBefore))
        );
    }

    @Test
    void shouldThrowAResourceNotFoundExceptionDuringAddingMealToCurrentDiaryIfMealDoesNotExist() {
        //given + when
        given(mealService.findByName("WillowPears")).willThrow(ResourceNotFoundException.class);
        //then
        assertThrows(ResourceNotFoundException.class, () -> foodDiaryService.addMealToCurrentDiary(mealooUser2, "WillowPears"));
    }


    @Test
    void shouldThrowAResourceNotFoundExceptionDuringDeletingMealFromCurrentDiaryIfMealDoesNotExistInDatabase() {
        //given + when
        given(mealService.findByName("WillowPears")).willThrow(ResourceNotFoundException.class);
        //then
        assertThrows(ResourceNotFoundException.class, () -> foodDiaryService.deleteMealFromCurrentDiary(mealooUser2, "WillowPears"));
    }

    @Test
    void shouldThrownAResourceNotFoundExceptionWhenDeletingMealFrocCurrentDiaryIfFoodDiaryDoesNotContainGivenMeal(){
        //given + when
        given(mealService.findByName("RiceAndStrawberry")).willReturn(meal2);
        //then
        assertThrows(ResourceNotFoundException.class, () -> foodDiaryService.deleteMealFromCurrentDiary(mealooUser2, "RiceAndStrawberry"));
    }
}