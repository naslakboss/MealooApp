package codebuddies.MealooApp.services;


import codebuddies.MealooApp.dataproviders.MealProvider;
import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
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
    MealProvider mealProvider;

    Product product1;
    Product product2;
    Product product3;

    Ingredient ingredient1;
    Ingredient ingredient2;
    Ingredient ingredient3;

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

        product1 = new Product("Rice", 5
                , new Macronutrients(7, 79, 1), 357, ProductType.GRAINS);
        product2 = new Product("Chicken", 12
                , new Macronutrients(22, 1, 4), 121, ProductType.MEAT);
        product3 = new Product("Strawberry", 8
                , new Macronutrients(1, 8, 0), 32, ProductType.GRAINS);

        ingredient1 = new Ingredient(100, product1);
        ingredient2 = new Ingredient(200, product2);
        ingredient3 = new Ingredient(500, product3);

        listOfIngredients1 = Arrays.asList(ingredient1, ingredient2);
        listOfIngredients2 = Arrays.asList(ingredient1, ingredient3);
        listOfIngredients3 = Arrays.asList(ingredient2, ingredient3);

        mealEntity1 = new Meal("RiceAndChicken", listOfIngredients1, MealDifficulty.MEDIUM);
        meal1 = mapper.map(mealEntity1, MealDTO.class);

        mealEntity2 = new Meal("RiceAndStrawberry", listOfIngredients2, MealDifficulty.EASY);
        meal2 = mapper.map(mealEntity2, MealDTO.class);

        mealEntity3 = new Meal("ChickenAndStrawberry", listOfIngredients3, MealDifficulty.INSANE);
        meal3 = mapper.map(mealEntity3, MealDTO.class);

        meals = List.of(meal1, meal2, meal3);

        mealService = new MealService(mealProvider, imageService, ingredientService);

    }

    public Page<MealDTO> createTestPage(Pageable pageable) {
        List<MealDTO> listOfMeals = meals;
        return new PageImpl<>(listOfMeals, pageable, listOfMeals.size());
    }

    @Test
    void shouldReturnMealList() {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        when(mealProvider.getAllMeals(pageable)).thenReturn(createTestPage(pageable));

        //when
        Page<MealDTO> listOfMeals = mealService.getAllMeals(pageable);

        //then
        assertAll(
                () -> assertThat(listOfMeals.getSize(), equalTo(3)),
                () -> assertThat(listOfMeals.getContent().get(2).getName(), equalTo("ChickenAndStrawberry"))
        );
    }

    @Test
    void shouldReturnProperMealIfExists() {
        //given
        given(mealProvider.getMealByName("RiceAndChicken")).willReturn(meal1);

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
        verify(mealProvider, times(1)).deleteByName("goodName");
    }

    @Test
    void shouldThrowEntityAlreadyFoundExceptionDuringCreatingMealIfMealOfGivenNameExists() {
        //given + when
        given(mealProvider.existsByName("RiceAndChicken")).willReturn(true);

        //then
        assertThrows(EntityAlreadyFoundException.class, () ->
                mealService.createMeal(meal1));
    }

    @Test
    void shouldCreateMealWithProperIngredientsIfMealDidNotExistBefore() {
        //given
        given(mealProvider.existsByName("RiceAndChicken")).willReturn(false);
        given(mealProvider.createMeal(meal1)).willReturn(meal1);

        //when
        MealDTO createdMeal = mealService.createMeal(meal1);

        //then
        assertAll(
                () -> verify(ingredientService, times(1)).createIngredients(meal1),
                () -> assertThat(createdMeal.getName(), equalTo("RiceAndChicken")),
                () -> assertThat(createdMeal.getIngredients().get(0).getProduct().getName(), equalTo("Rice")),
                () -> assertThat(createdMeal.getIngredients().get(1).getAmount(), equalTo(ingredient2.getAmount()))
        );
    }

    @Test
    void shouldUpdateMeal() {
        //given
        given(mealProvider.updateMeal(meal2)).willReturn(meal2);

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
         given(mealProvider.getMealByName("RiceAndChicken")).willReturn(meal1);

        //when
        MealDTO meal = mealService.addImageToMeal("RiceAndChicken", "filePath");

        //then
        verify(imageService, times(1)).createNewImage(meal1, "filePath");
    }


    @Test
    void shouldDeleteImageFromMealIfMealDoesExist() {
        //given
        ImageDTO image = new ImageDTO("fileUrl");
        given(imageService.getImageByFileUrl("fileUrl")).willReturn(image);

        List<ImageDTO> imagesList = new ArrayList<>();
        imagesList.add(image);
        meal1.setImages(imagesList);

        given(mealProvider.getMealByName("RiceAndChicken")).willReturn(meal1);
        //when

        mealService.deleteImageFromMeal("RiceAndChicken", "fileUrl");
        //then
        assertThat(meal1.getImages(), Matchers.not(contains(image)));
    }

    @Test
    void shouldReturnNamesOfMatchingMeals() {
        //given
        int perfectCaloricValue = 500;
        given(mealProvider.findNamesOfMatchingMeals(perfectCaloricValue - 100, perfectCaloricValue + 100))
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

