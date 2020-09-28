package codebuddies.MealooApp.services;


import codebuddies.MealooApp.datamappers.MealMapper;
import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class MealServiceTest {

    ModelMapper mapper;

    @Mock
    ImageService imageService;

    @Mock
    IngredientService ingredientService;

    @Mock
    MealMapper mealMapper;

    Product productEntity1;
    Product productEntity2;
    Product productEntity3;

    ProductForIngredientDTO product1;
    ProductForIngredientDTO product2;
    ProductForIngredientDTO product3;

    Ingredient ingredientEntity1;
    Ingredient ingredientEntity2;
    Ingredient ingredientEntity3;

    IngredientForMealDTO ingredient1;
    IngredientForMealDTO ingredient2;
    IngredientForMealDTO ingredient3;

    List<Ingredient> listOfIngredients1;
    List<Ingredient> listOfIngredients2;
    List<Ingredient> listOfIngredients3;

    Meal mealEntity1;
    Meal mealEntity2;
    Meal mealEntity3;

    MealDTO meal1;
    MealDTO meal2;
    MealDTO meal3;

    List<MealDTO> meals;

    MealService mealService;

    @BeforeEach
    public void setUp() {

        mapper = new ModelMapper();

        productEntity1 = new Product("Rice", 5
                , new Macronutrients(7, 79, 1), 357, ProductType.GRAINS);
        productEntity2 = new Product("Chicken", 12
                , new Macronutrients(22, 1, 4), 121, ProductType.MEAT);
        productEntity3 = new Product("Strawberry", 8
                , new Macronutrients(1, 8, 0), 32, ProductType.GRAINS);

        product1 = mapper.map(productEntity1, ProductForIngredientDTO.class);
        product2 = mapper.map(productEntity2, ProductForIngredientDTO.class);
        product3 = mapper.map(productEntity3, ProductForIngredientDTO.class);

        ingredientEntity1 = new Ingredient(100, productEntity1);
        ingredientEntity2 = new Ingredient(200, productEntity2);
        ingredientEntity3 = new Ingredient(500, productEntity3);

        ingredient1 = mapper.map(ingredientEntity1, IngredientForMealDTO.class);
        ingredient2 = mapper.map(ingredientEntity2, IngredientForMealDTO.class);
        ingredient3 = mapper.map(ingredientEntity3, IngredientForMealDTO.class);

        listOfIngredients1 = Arrays.asList(ingredientEntity1, ingredientEntity2);
        listOfIngredients2 = Arrays.asList(ingredientEntity1, ingredientEntity3);
        listOfIngredients3 = Arrays.asList(ingredientEntity2, ingredientEntity3);

        mealEntity1 = new Meal("RiceAndChicken", listOfIngredients1, 2.9, MealDifficulty.MEDIUM, "Example recipe",
                new MealMacronutrients(50, 60, 70), 609, new ArrayList<>(), new ArrayList<>());
        meal1 = mapper.map(mealEntity1, MealDTO.class);

        mealEntity2 = new Meal("RiceAndStrawberry", listOfIngredients2, 15, MealDifficulty.EASY, "Example recipe2",
                new MealMacronutrients(40, 30, 25), 200, new ArrayList<>(), new ArrayList<>());
        meal2 = mapper.map(mealEntity1, MealDTO.class);
        mealEntity3 = new Meal("ChickenAndStrawberry", listOfIngredients3, 30, MealDifficulty.INSANE, "Example recipe3",
                new MealMacronutrients(30, 31, 32), 700, new ArrayList<>(), new ArrayList<>());
        meal3 = mapper.map(mealEntity1, MealDTO.class);
        meals = List.of(meal1, meal2, meal3);

        mealService = new MealService(mealMapper, imageService, ingredientService);

    }

    public Page<MealDTO> createTestPage(Pageable pageable) {
        List<MealDTO> listOfMeals = meals;
        return new PageImpl<>(listOfMeals, pageable, listOfMeals.size());
    }

    @Test
    void shouldReturnMealList() {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        when(mealMapper.getAllMeals(pageable)).thenReturn(createTestPage(pageable));

        //when
        Page<MealDTO> listOfMeals = mealService.getAllMeals(pageable);

        //then
        assertAll(
                () -> assertThat(listOfMeals.getSize(), equalTo(3)),
                () -> assertThat(listOfMeals.getContent().get(2).getName(), equalTo("RiceAndChicken"))
        );
    }

    @Test
    void shouldReturnProperMealIfExists() {
        //given
        given(mealMapper.getMealByName("RiceAndChicken")).willReturn(meal1);

        //when
        MealDTO meal = mealService.getMealByName("RiceAndChicken");

        //then
        assertThat(meal, equalTo(meal1));
    }

    @Test
    void shouldDeleteMealIfExists() {
        //given + when
        mealService.deleteMealByName("goodName");

        //then
        verify(mealMapper, times(1)).deleteByName("goodName");
    }

    @Test
    void shouldThrowEntityAlreadyFoundExceptionDuringCreatingMealIfMealOfGivenNameExists() {
        //given + when
        given(mealMapper.existsByName("RiceAndChicken")).willReturn(true);

        //then
        assertThrows(EntityAlreadyFoundException.class, () ->
                mealService.createMeal(meal1));
    }

    @Test
    void shouldCreateMealWithProperIngredientsIfMealDidNotExistBefore() {
        //given
        given(mealMapper.existsByName("RiceAndChicken")).willReturn(false);
        given(mealMapper.createMeal(meal1)).willReturn(meal1);

        //when
        MealDTO createdMeal = mealService.createMeal(meal1);

        //then
        assertAll(
                () -> verify(ingredientService, times(1)).createIngredients(meal1),
                () -> assertThat(createdMeal.getName(), equalTo("RiceAndChicken")),
                () -> assertThat(createdMeal.getIngredients().get(0).getProduct().getName(), equalTo("Rice")),
                () -> assertThat(createdMeal.getIngredients().get(1).getAmount(), equalTo(ingredientEntity2.getAmount()))
        );
    }

    @Test
    void shouldUpdateMeal() {
        //given
        given(mealMapper.updateMeal(meal2)).willReturn(meal2);

        //when
        MealDTO updatedMeal = mealService.updateMealByName(meal2, "RiceAndChicken");

        //then
        assertAll(
                () -> assertThat(updatedMeal.getName(), equalTo("RiceAndChicken")),
                () -> assertThat(updatedMeal.getIngredients(), equalTo(meal2.getIngredients()))
        );
    }

    @Test
    void shouldCreateNewImageWhenNameOfMealIsCorrect() {
        //given
         given(mealMapper.getMealByName("RiceAndChicken")).willReturn(meal1);

        //when
        mealService.addImageToMeal("RiceAndChicken", "filePath");

        //then
        verify(imageService, times(1)).createNewImage(meal1, "filePath");
    }

    @Test
    void shouldCalculateProteinsCorrectly() {
        //given
        int totalProteinsExpectation = ingredient1.getProduct()
                .getMacronutrients().getProteinsPer100g() * ingredient1.getAmount() / 100 +
                ingredient2.getProduct().getMacronutrients().getProteinsPer100g() * ingredient2.getAmount() / 100;
        //when
        int result = mealService.calculateProteins(List.of(ingredient1, ingredient2));

        //then
        assertThat(result, equalTo(totalProteinsExpectation));
    }

    @Test
    void shouldCalculateCarbohydratesCorrectly() {
        //given
        int totalCarbohydratesExpectation = ingredient1.getProduct()
                .getMacronutrients().getCarbohydratesPer100g() * ingredient1.getAmount() / 100 +
                ingredient2.getProduct().getMacronutrients().getCarbohydratesPer100g() * ingredient2.getAmount() / 100;
        //when
        int result = mealService.calculateCarbohydrates(List.of(ingredient1, ingredient2));

        //then
        assertThat(result, equalTo(totalCarbohydratesExpectation));
    }

    @Test
    void shouldCalculateFatsCorrectly() {
        //given
        int totalFatsExpectation = ingredient1.getProduct()
                .getMacronutrients().getFatsPer100g() * ingredient1.getAmount() / 100 +
                ingredient2.getProduct().getMacronutrients().getFatsPer100g() * ingredient2.getAmount() / 100;

        //when
        int result = mealService.calculateFats(List.of(ingredient1, ingredient2));

        //then
        assertThat(result, equalTo(totalFatsExpectation));
    }

    @Test
    void shouldSetProperMealMacronutrients(){
        //given
        meal1.setMealMacronutrients(new MealMacronutrients(0, 0,0));

        //when
        mealService.calculateMealMacronutrients(meal1);

        //then
        assertAll(
                () -> assertThat(meal1.getMealMacronutrients().getTotalProteins()
                        , equalTo(mealService.calculateProteins(meal1.getIngredients()))),
                () -> assertThat(meal1.getMealMacronutrients().getTotalCarbohydrates()
                        , equalTo(mealService.calculateCarbohydrates(meal1.getIngredients()))),
                () -> assertThat(meal1.getMealMacronutrients().getTotalFats()
                        , equalTo(mealService.calculateFats(meal1.getIngredients())))
        );
    }

    @Test
    void shouldCalculatePriceProperly(){
        //given
        double oldPrice = meal1.getPrice();
        meal1.setPrice(0);

        //when
        mealService.calculatePrice(meal1);

        //then
        assertThat(meal1.getPrice(), not(equalTo(0)));
        assertThat(meal1.getPrice(), equalTo(oldPrice));
    }

    @Test
    void shouldCalculateTotalCaloriesProperly(){
        //given
        int oldTotalCalories = meal1.getTotalCalories();
        meal1.setTotalCalories(0);

        //when
        mealService.calculateTotalCalories(meal1);

        //then
        assertThat(meal1.getTotalCalories(), not(equalTo(0)));
        assertThat(meal1.getTotalCalories(), (equalTo(oldTotalCalories)));
    }

    @Test
    void shouldDeleteImageFromMealIfMealDoesExist() {
        //given
        ImageDTO image = new ImageDTO("fileUrl");
        given(imageService.getImageByFileUrl("fileUrl")).willReturn(image);

        List<ImageDTO> imagesList = new ArrayList<>();
        imagesList.add(image);
        meal1.setImages(imagesList);

        given(mealMapper.getMealByName("RiceAndChicken")).willReturn(meal1);
        //when

        mealService.deleteImageFromMeal("RiceAndChicken", "fileUrl");
        //then
        assertThat(meal1.getImages(), Matchers.not(contains(image)));
    }

    @Test
    void shouldReturnNamesOfMatchingMeals() {
        //given
        int perfectCaloricValue = 500;
        given(mealMapper.findNamesOfMatchingMeals(perfectCaloricValue - 100, perfectCaloricValue + 100))
                .willReturn(meals);

        //when
        List<String> matchingMealsNames = mealService.findNamesOfMatchingMeals(perfectCaloricValue);

        //then
        assertAll(
                () -> assertThat(matchingMealsNames.get(0), is(meals.get(0).getName())),
                () -> assertThat(matchingMealsNames.get(1), is(meals.get(1).getName())),
                () -> assertThat(matchingMealsNames.get(2), is(meals.get(2).getName()))
        );

    }
}

