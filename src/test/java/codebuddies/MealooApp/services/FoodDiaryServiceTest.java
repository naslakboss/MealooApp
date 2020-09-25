package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.FoodDiaryProvider;
import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.RequiredMealsNotFoundException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.util.Assert.doesNotContain;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class FoodDiaryServiceTest {

    ModelMapper modelMapper;

    @Mock
    FoodDiaryProvider diaryProvider;

    @Mock
    MealooUserService userService;

    @Mock
    MealService mealService;

    Product product1;
    Product product2;
    Product product3;

    IngredientForMealDTO ingredient1;
    IngredientForMealDTO ingredient2;
    IngredientForMealDTO ingredient3;

    List<IngredientForMealDTO> listOfIngredients1;
    List<IngredientForMealDTO> listOfIngredients2;
    List<IngredientForMealDTO> listOfIngredients3;

    MealDTO meal1;
    MealDTO meal2;
    MealDTO meal3;

    List<MealDTO> listOfMeals1;
    List<MealDTO> listOfMeals2;
    List<MealDTO> listOfMeals3;

    MealooUserDTO user1;
    MealooUserDTO user2;

    FoodDiaryDTO foodDiary1;
    FoodDiaryDTO foodDiary2;
    FoodDiaryDTO foodDiary3;

    List<FoodDiaryDTO> listOfDiaries;

    FoodDiaryService diaryService;

    @BeforeEach
    void setUp(){
        modelMapper = new ModelMapper();
        product1 = new Product("Rice", 7, new Macronutrients(7, 79, 1), 357, ProductType.GRAINS);
        product2 = new Product("Chicken", 13, new Macronutrients(22, 1, 4), 121, ProductType.MEAT);
        product3 = new Product("Strawberry", 12, new Macronutrients(1, 8, 0), 35, ProductType.GRAINS);

        ingredient1 = modelMapper.map(new Ingredient(100, product1), IngredientForMealDTO.class);
        ingredient2 = modelMapper.map(new Ingredient(200, product2), IngredientForMealDTO.class);
        ingredient3 = modelMapper.map(new Ingredient(500, product3), IngredientForMealDTO.class);

        listOfIngredients1 = Arrays.asList(ingredient1, ingredient2);
        listOfIngredients2 = Arrays.asList(ingredient1, ingredient3);
        listOfIngredients3 = Arrays.asList(ingredient2, ingredient3);

        meal1 = new MealDTO("Rice and Chicken", listOfIngredients1, 3.3, MealDifficulty.HARD
                    ,new MealMacronutrients(52, 79, 9), 605, "Example recipe 1", Collections.emptyList());

        meal2 = new MealDTO("Rice and Strawberry", listOfIngredients2, 6.7, MealDifficulty.EASY
                ,new MealMacronutrients(13, 119, 1), 537, "Example recipe 2", Collections.emptyList());

        meal3 = new MealDTO("RiceAndChicken", listOfIngredients3, 8.6, MealDifficulty.INSANE
                ,new MealMacronutrients(49, 40, 8), 428, "Example recipe 3", Collections.emptyList());

        listOfMeals1 = new ArrayList<>();
        listOfMeals1.add(meal1);
        listOfMeals1.add(meal2);

        listOfMeals2 = new ArrayList<>();
        listOfMeals2.add(meal2);
        listOfMeals2.add(meal3);

        listOfMeals3 = new ArrayList<>();
        listOfMeals3.add(meal1);
        listOfMeals3.add(meal3);

        user1 = new MealooUserDTO(1L, "Admin", "pass", MealooUserRole.ADMIN, "admin@gmail.com"
                ,new NutritionSettings(3500)
                , new MealooUserDetails(180, 90, 22, Sex.MALE, PhysicalActivity.HIGH));

        user2 = new MealooUserDTO(2L, "User", "secret", MealooUserRole.USER, "user@gmail.com"
                ,new NutritionSettings(2500)
                , new MealooUserDetails(170, 80, 27, Sex.FEMALE, PhysicalActivity.LITTLE));

        foodDiary1 = new FoodDiaryDTO(1L, LocalDate.now(), listOfMeals1, new MealMacronutrients(100, 100, 100), 1000, 13.7);
        foodDiary2 = new FoodDiaryDTO(2L, LocalDate.now(), listOfMeals2, new MealMacronutrients(90, 90, 90), 900, 60.0);
        foodDiary3 = new FoodDiaryDTO(3L, LocalDate.now(), listOfMeals3, new MealMacronutrients(150, 150, 150), 150, 100.0);

        listOfDiaries = new ArrayList<>();
        listOfDiaries.add(foodDiary1);
        listOfDiaries.add(foodDiary2);
        listOfDiaries.add(foodDiary3);

        diaryService = new FoodDiaryService(diaryProvider, userService, mealService);

    }

    Page<FoodDiaryDTO> createTestPage(List<FoodDiaryDTO> foodDiaries, Pageable pageable){
        List<FoodDiaryDTO> diaries = listOfDiaries;
        return new PageImpl<>(diaries, pageable, diaries.size());
    }

    @Test
    void shouldReturnAllDiaries() {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        given(diaryService.getAllDiaries("Admin", pageable)).willReturn(createTestPage(listOfDiaries, pageable));
        //when
        Page<FoodDiaryDTO> result = diaryService.getAllDiaries("Admin", pageable);
        //then
        assertAll(
                () -> assertThat(result.getContent().get(0), is(listOfDiaries.get(0))),
                () -> assertThat(result.getContent().get(1), is(listOfDiaries.get(1))),
                () -> assertThat(result.getContent().get(2), is(listOfDiaries.get(2)))
        );

    }

    @Test
    void shouldFindCurrentDiaryForUser(){
        //given
        LocalDate currentDate = LocalDate.now();
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        given(diaryProvider.getDiaryByDate(user1, currentDate)).willReturn(foodDiary1);
        //when
        FoodDiaryDTO currentDiary = diaryService.getCurrentDiary("Admin");
        //then
        assertThat(currentDiary, sameInstance(foodDiary1));
    }

    @Test
    void shouldFindDiaryForUserByDate(){
        //given
        String sDate = "2020-08-20";
        LocalDate date = LocalDate.parse(sDate);
        foodDiary2.setDate(date);
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        given(diaryProvider.getDiaryByDate(user1, date)).willReturn(foodDiary2);
        //when
        FoodDiaryDTO diary = diaryService.getDiaryByDate("Admin", "2020-08-20");
        //then
        assertThat(diary, sameInstance(foodDiary2));
    }

    @Test
    void shouldCreateNewDiary(){
        //given
        LocalDate currentDate = LocalDate.now();
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        given(diaryProvider.createDiary(user1, currentDate))
                .willReturn(new FoodDiaryDTO(5L, currentDate, Collections.emptyList()
                            ,new MealMacronutrients(0,0,0), 0, 0.0));
        //when
        FoodDiaryDTO newDiary = diaryService.createDiary("Admin");
        //then
        assertAll(
                () -> assertThat(newDiary.getListOfMeals().size(), equalTo(0)),
                () -> assertThat(newDiary.getTotalCalories(), equalTo(0)),
                () -> assertThat(newDiary.getTotalCalories(), equalTo(0))
        );

    }

    @Test
    void shouldRecalculateMacronutrientsAccordingToMeals(){
        //given
        int totalProteinsBefore = foodDiary1.getMealMacronutrients().getTotalProteins();
        int totalCarbohydratesBefore = foodDiary1.getMealMacronutrients().getTotalCarbohydrates();
        int totalFatesBefore = foodDiary1.getMealMacronutrients().getTotalFats();
        foodDiary1.getListOfMeals().get(0).getIngredients().get(0).setAmount(1000);
        //when
        diaryService.recalculateData(foodDiary1);
        //then
        assertAll(
                () -> assertThat(totalProteinsBefore, not(equalTo(foodDiary1.getMealMacronutrients().getTotalProteins()))),
                () -> assertThat(totalCarbohydratesBefore, not(equalTo(foodDiary1.getMealMacronutrients().getTotalCarbohydrates()))),
                () -> assertThat(totalFatesBefore, not(equalTo(foodDiary1.getMealMacronutrients().getTotalFats())))
        );
    }

    @Test
    void shouldRecalculatePriceAccordingToMeals(){
        //given
        double totalPriceBefore = foodDiary1.getTotalPrice();
        foodDiary1.getListOfMeals().get(0).setPrice(7);
        //when
        diaryService.recalculatePrice(foodDiary1);
        //then
        assertThat(totalPriceBefore, is(foodDiary1.getTotalPrice()));
    }

    @Test
    void shouldRecalculateTotalCaloriesAccordingToMeals(){
        //given
        double totalPriceBefore = foodDiary1.getTotalCalories();
        foodDiary1.setTotalCalories(0);
        //when
        diaryService.recalculatePrice(foodDiary1);
        //then
        assertThat(totalPriceBefore, is(not(0)));
    }

    @Test
    void shouldAddMeal(){
        //given
        LocalDate currentDate = LocalDate.now();

        FoodDiaryDTO foodDiary4 = new FoodDiaryDTO(6L, LocalDate.now(), List.of(meal1, meal2, meal3)
                , new MealMacronutrients(150, 150, 150), 150, 100.0);
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        given(mealService.getMealByName("Chicken and Strawberries")).willReturn(meal3);
        given(diaryProvider.updateDiary(foodDiary1, user1)).willReturn(foodDiary4);
        given(diaryProvider.getDiaryByDate(user1, currentDate)).willReturn(foodDiary1);
        //when
        FoodDiaryDTO diary4 = diaryService.addMeal("Admin", "Chicken and Strawberries");
        //then
        assertAll(
                () -> assertThat(diary4.getListOfMeals().size(),equalTo(foodDiary4.getListOfMeals().size())),
                () -> assertThat(diary4.getListOfMeals(), hasItem(meal3))
        );

    }

    @Test
    void shouldDeleteMeal(){
        //given
        LocalDate currentDate = LocalDate.now();
        List<MealDTO> listOfAll = new ArrayList<>();
        listOfAll.add(meal1);
        listOfAll.add(meal2);
        listOfAll.add(meal3);
        FoodDiaryDTO foodDiary4 = new FoodDiaryDTO(6L, LocalDate.now(), listOfAll
                , new MealMacronutrients(150, 150, 150), 150, 100.0);
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        given(mealService.getMealByName("Chicken and Strawberries")).willReturn(meal3);
        given(diaryProvider.updateDiary(foodDiary4, user1)).willReturn(foodDiary1);
        given(diaryProvider.getDiaryByDate(user1, currentDate)).willReturn(foodDiary4);
        //when
        FoodDiaryDTO diary4 = diaryService.addMeal("Admin", "Chicken and Strawberries");
        //then
        assertAll(
                () -> assertThat(diary4.getListOfMeals().size(),equalTo(foodDiary1.getListOfMeals().size())),
                () -> assertThat(diary4.getListOfMeals(), not(hasItem(meal3)))
        );
    }

    @Test
    void shouldReturnNamesOfMealsUsedThreeDaysBack(){
        //given
        LocalDate threeDaysBack = LocalDate.now().minusDays(3);;
        given(diaryProvider.rejectMealsFromThreeDaysBack(user1, threeDaysBack)).willReturn(List.of(foodDiary1, foodDiary3));
        //when
        List<String> listOfNames = diaryService.rejectMealsFromThreeDaysBack(user1);
        //then
        assertAll(
                () -> assertThat(listOfNames.size(), equalTo(4)),
                () -> assertThat(listOfNames, hasItem("Rice and Strawberry"))
        );
    }

    @Test
    void shouldThrowRunTimeExceptionIfAmountOfCaloriesIsLessThanZero(){
        //given + when
        int totalCalories = -5;
        int numberOfMeals = 5;
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        // then
        assertThrows(RuntimeException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, user1.getUsername()));
    }

    @Test
    void shouldThrowRunTimeExceptionIfAmountOfCaloriesIsGreaterThanTenThousands(){
        //given + when
        int totalCalories = 100001;
        int numberOfMeals = 5;
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        // then
        assertThrows(RuntimeException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, user1.getUsername()));
    }

    @Test
    void shouldThrowRunTimeExceptionIfNumberOfMealsIsLessThanThree(){
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 2;
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        // then
        assertThrows(RuntimeException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, user1.getUsername()));
    }

    @Test
    void shouldThrowRunTimeExceptionIfNumberOfMealsIsGreaterThan7(){
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 10;
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        // then
        assertThrows(RuntimeException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, user1.getUsername()));
    }

    @Test
    void shouldThrowRunTimeExceptionIfAnyMealsExistInCurrentDiary(){
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 5;
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        // then
        assertThrows(RuntimeException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, user1.getUsername()));
    }

    @Test
    void shouldThrowRequiredMealsNotFoundExceptionIfListOfMealsForGeneratorIsTooSmall(){
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 5;
        given(userService.getUserByUsername("Admin")).willReturn(user1);
        given(diaryProvider.getDiaryByDate(user1, LocalDate.now())).willReturn(foodDiary1);
        foodDiary1.setListOfMeals(Collections.emptyList());
        given(mealService.findNamesOfMatchingMeals(500)).willReturn(Collections.emptyList());
        // then
        assertThrows(RequiredMealsNotFoundException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, user1.getUsername()));
    }

    @Test
    void shouldDiscardMismatchedMeals(){
        //given
        List<String> matchingMeals = new ArrayList<>();
        matchingMeals.add("Rice and Chicken");
        matchingMeals.add("Scrambled Eggs");
        matchingMeals.add("Rice and Strawberry");
        matchingMeals.add("Hamburger");
        List<String> rejectedMeals = List.of("Rice and Chicken", "Rice and Strawberry");
        //when
        List<String> result = diaryService.discardMismatchedMeals(matchingMeals, rejectedMeals);
        //then
        assertAll(
                () -> assertThat(result, not(hasItem("Rice and Strawberry"))),
                () -> assertThat(result, not(hasItem("Rice and Chicken")))
        );
    }


}