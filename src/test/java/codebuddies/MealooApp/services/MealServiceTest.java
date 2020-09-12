package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.entities.user.NutritionSettings;
import codebuddies.MealooApp.exceptions.IllegalDataException;
import codebuddies.MealooApp.exceptions.MealIsNeededException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.IngredientRepository;
import codebuddies.MealooApp.repositories.MealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class MealServiceTest {

    @Mock
    ProductService productService;
    @Mock
    MealRepository mealRepository;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    ImageService imageService;

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

    MealService mealService;

    @BeforeEach
    public void setUp(){
        product1 = new Product("Rice", 5
                , new Macronutrients(7, 79, 1), ProductType.GRAINS);
        product2 = new Product("Chicken", 12
                , new Macronutrients(22, 1, 4), ProductType.MEAT);
        product3 = new Product("Strawberry", 8
                , new Macronutrients(1, 8, 0), ProductType.GRAINS);

        ingredient1 = new Ingredient(100, product1);
        ingredient2 = new Ingredient(200, product2);
        ingredient3 = new Ingredient(500, product3);

        listOfIngredients1 = Arrays.asList(ingredient1, ingredient2);
        listOfIngredients2 = Arrays.asList(ingredient1, ingredient3);
        listOfIngredients3 = Arrays.asList(ingredient2, ingredient3);

        meal1 = new Meal("RiceAndChicken", listOfIngredients1, MealDifficulty.MEDIUM);
        meal2 = new Meal("RiceAndStrawberry", listOfIngredients2, MealDifficulty.EASY);
        meal3 = new Meal("ChickenAndStrawberry", listOfIngredients3, MealDifficulty.INSANE);

        mealService = new MealService(productService, mealRepository, ingredientRepository, imageService);

    }

    @Test
    void shouldReturnMealList() {
        //given
        when(mealRepository.findAll()).thenReturn(Arrays.asList(meal1,meal2,meal3));
        //when
        List<Meal> listOfMeals = mealService.findAll();
        //then
        assertAll(
                () -> assertThat(listOfMeals.size(), equalTo(3)),
                () -> assertThat(listOfMeals.get(0), equalTo(meal1)),
                () -> assertThat(listOfMeals.get(2).getName(), equalTo("ChickenAndStrawberry"))
        );
    }

    @Test
    void shouldReturnFalseIfMealDoesNotExist() {
        //given + when
        when(mealRepository.existsByName("BadName")).thenReturn(false);
        //then
        assertFalse(mealService.existsByName("BadName"));
    }

    @Test
    void shouldReturnTrueIfMealExists() {
        //given + when
        when(mealRepository.existsByName("BadName")).thenReturn(true);
        //then
        assertTrue(mealService.existsByName("BadName"));
    }

    @Test
    void shouldReturnProperMealIfExists(){
        //given
        given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);
        //when
        Meal meal = mealService.findByName("RiceAndChicken");
        //then
        assertThat(meal, equalTo(meal1));
    }

    @Test
    void shouldThrowAnExceptionWhenMealDoesNotExist(){
        //given + when
        when(mealRepository.findByName("BadName")).thenReturn(null);
        //then
        assertThrows(ResourceNotFoundException.class, () -> mealService.findByName("BadName"));
    }

    @Test
    void shouldCreateListOfIngredients() {
        //given + when
        List<Ingredient> listOfIngredients = mealService.createListOfIngredients(meal2);
        //then
        assertAll(
                () -> assertThat(listOfIngredients.size(), equalTo(2)),
                () -> verify(ingredientRepository, times(2)).save(any())
        );
    }
    // todo Improve this test

    @Test
    void shouldThrowAnIllegalDataExceptionWhenAmountOfProductsIsLessThan1(){
        //given + when
        Ingredient ingredient4 = new Ingredient(0, product1);
        Meal badMeal = new Meal("BadCMeal", Collections.singletonList(ingredient4), MealDifficulty.EASY);
        //then
        assertThrows(IllegalDataException.class, () -> mealService.createListOfIngredients(badMeal));
    }

    @Test
    void shouldThrowAnResourceNotFoundExceptionWhenProductDoesNotExist(){
        //given + when
        when(productService.findByName(anyString())).thenThrow(ResourceNotFoundException.class);
        //then
        assertThrows(ResourceNotFoundException.class, () -> mealService.createListOfIngredients(meal1));
    }
    @Test
    void shouldSaveMeal(){
        //given
        given(productService.findByName("Rice")).willReturn(product1);
        given(productService.findByName("Chicken")).willReturn(product2);
        //when
        Meal savedMeal = mealService.save(meal1);
        //then
        verify(mealRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowAMealIsNeededExceptionIfMealIsCreatedFoodDiary() {
        //given + when
        given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);
        meal1.setFoodDiaries(Collections.singletonList(
                new FoodDiary(Arrays.asList(meal2,meal3), LocalDate.now()
                        ,new MealooUser("Tester", "assertAll", "24/7@gmail.com"))));
        //then
        assertThrows(MealIsNeededException.class, () -> mealService.updateByName("RiceAndChicken", meal2));
    }

    @Test
    void shouldUpdateDataAndRecalculateAutomatically(){
        //given
        given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);
        meal1.setFoodDiaries(Collections.emptyList());
        Ingredient ingredient4 = new Ingredient(800, product2);
        Meal updateData = new Meal("CHICKENAndRice", Arrays.asList(ingredient1, ingredient4), MealDifficulty.INSANE);
        given(productService.findByName("Rice")).willReturn(product1);
        given(productService.findByName("Chicken")).willReturn(product2);
        //when
        Meal updatedMeal = mealService.updateByName("RiceAndChicken", updateData);
        //then
        assertAll(
                () -> assertThat(updatedMeal.getName(), equalTo("CHICKENAndRice")),
                () -> assertThat(updatedMeal.getMealDifficulty() , equalTo(MealDifficulty.INSANE)),
                () -> assertThat(updatedMeal.getIngredients().get(1).getAmount(), equalTo(800))
        );

    }

    @Test
    void shouldThrowAMealIsNeededExceptionIfMealIsCreatedDiary() {
        //given + when
        given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);
        meal1.setFoodDiaries(Collections.singletonList(
                new FoodDiary(Arrays.asList(meal2,meal3), LocalDate.now()
                        ,new MealooUser("Tester", "assertAll", "24/7@gmail.com"))));
        //then
        assertThrows(MealIsNeededException.class, () -> mealService.deleteByName("RiceAndChicken"));
    }

    @Test
    void shouldThrowAResourceNotFoundExceptionIfMealIsNotExists(){
        //given + when
        given(mealRepository.findByName("Cola")).willThrow(ResourceNotFoundException.class);
        //then
        assertThrows(ResourceNotFoundException.class, () -> mealService.deleteByName("Cola"));
    }
    @Test
    void shouldDeleteMealIfMealExistAndDoesNotBuildAnyDiary(){
        //given
        meal1.setFoodDiaries(Collections.emptyList());
        given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);
        doNothing().when(mealRepository).deleteByName("RiceAndChicken");
        //when
        mealService.deleteByName("RiceAndChicken");
        //then
        verify(mealRepository, times(1)).deleteByName(anyString());
    }

    @Test
    void shouldReturnMealNamesIfTheDifferenceInCaloriesIsBetween100lessOrMoreThanTheIdeaValue(){
        //given
        given(mealRepository.findAll()).willReturn(Arrays.asList(meal1, meal2, meal3));
        meal1.setTotalCalories(300);
        meal2.setTotalCalories(550);
        meal3.setTotalCalories(602);
        int perfectCaloricValue = 500;
        //when
        List<String> matchingMeals = mealService.findAllNamesOfMatchingMeals(perfectCaloricValue);
        //then
        assertAll(
                () -> assertThat(matchingMeals.size(), equalTo(1)),
                () -> assertThat(matchingMeals.get(0), equalTo( meal2.getName()))
        );
    }

    @Test
    public void shouldAddImageToMeal() throws IOException {
        //given
        given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);

        Map result = new HashMap();
        result.put("filePath", "example//path//1");
        result.put("url", "example//url.com");

        given(imageService.addNewImage(anyString())).willReturn(result);
        Image image = new Image(result.get("filePath").toString(), result.get("url").toString(), meal1);

        //when
        mealService.addImageToMeal("RiceAndChicken", "filePath");
        //then
        assertAll(
                () -> assertThat(image.getMeal(), equalTo(meal1)),
                () -> assertThat(image.getFilePath(), equalTo("example//path//1")),
                () -> verify(imageService, times(1)).addNewImage("filePath"),
                () -> verify(imageService, times(1)).save(any())
        );
    }
        @Test
        public void shouldDeleteImageFromMealIfUrlIsCorrect() throws IOException {
            //given
            given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);
            Image image = new Image("path", "url", meal1);
            meal1.setImages(Collections.singletonList(image));
            //when
            mealService.deleteImageFromMeal(meal1.getName(), "url");
            //then
            verify(imageService, times(1)).deleteByFileUrl("url");
        }

        @Test
        public void shouldThrowResourceNotFoundExceptionWhenMealDoesNotHaveAnImage() {
        //given + when
        given(mealRepository.findByName("RiceAndChicken")).willReturn(meal1);
        meal1.setImages(Collections.emptyList());
        //then
        assertThrows(ResourceNotFoundException.class, () ->
                mealService.deleteImageFromMeal(meal1.getName(), anyString()));
        }

}
