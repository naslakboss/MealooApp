package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.FoodDiaryMapper;
import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.dto.IngredientForMealDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.meal.MealDifficulty;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.product.Ingredient;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.exceptions.RequiredMealsNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class FoodDiaryServiceTest {

    ModelMapper modelMapper;

    @Mock
    FoodDiaryMapper diaryProvider;

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
    void setUp() throws NoSuchAlgorithmException {

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
                , new MealMacronutrients(52, 79, 9), 605, "Example recipe 1", Collections.emptyList());

        meal2 = new MealDTO("Rice and Strawberry", listOfIngredients2, 6.7, MealDifficulty.EASY
                , new MealMacronutrients(13, 119, 1), 537, "Example recipe 2", Collections.emptyList());

        meal3 = new MealDTO("RiceAndChicken", listOfIngredients3, 8.6, MealDifficulty.INSANE
                , new MealMacronutrients(49, 40, 8), 428, "Example recipe 3", Collections.emptyList());

        listOfMeals1 = new ArrayList<>();
        listOfMeals1.add(meal1);
        listOfMeals1.add(meal2);

        listOfMeals2 = new ArrayList<>();
        listOfMeals2.add(meal2);
        listOfMeals2.add(meal3);

        listOfMeals3 = new ArrayList<>();
        listOfMeals3.add(meal1);
        listOfMeals3.add(meal3);

        foodDiary1 = new FoodDiaryDTO(LocalDate.now(), listOfMeals1, new MealMacronutrients(100, 100, 100), 1000, 13.7, user1);
        foodDiary2 = new FoodDiaryDTO(LocalDate.now(), listOfMeals2, new MealMacronutrients(90, 90, 90), 900, 60.0, user2);
        foodDiary3 = new FoodDiaryDTO(LocalDate.now(), listOfMeals3, new MealMacronutrients(150, 150, 150), 150, 100.0, user2);

        user1 = new MealooUserDTO(1L, "Admin", "pass", Collections.singletonList(new Role(MealooUserRole.ROLE_ADMIN)), "admin@gmail.com"
                , new NutritionSettings(3500)
                , new MealooUserDetails(180, 90, 22, Sex.MALE, PhysicalActivity.HIGH), List.of(foodDiary1, foodDiary2));

        user2 = new MealooUserDTO(2L, "User", "secret", Collections.singletonList(new Role(MealooUserRole.ROLE_USER)), "user@gmail.com"
                , new NutritionSettings(2500)
                , new MealooUserDetails(170, 80, 27, Sex.FEMALE, PhysicalActivity.LITTLE), List.of(foodDiary2, foodDiary3));

        listOfDiaries = new ArrayList<>();
        listOfDiaries.add(foodDiary1);
        listOfDiaries.add(foodDiary2);
        listOfDiaries.add(foodDiary3);

        diaryService = new FoodDiaryService(diaryProvider, userService, mealService);

    }

    Page<FoodDiaryDTO> createTestPage(List<FoodDiaryDTO> listOfDiaries, Pageable pageable) {
        return new PageImpl<>(listOfDiaries, pageable, listOfDiaries.size());
    }

    @Test
    @DisplayName("Check if all diaries for given user are returned")
    void shouldReturnAllDiaries() {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        given(diaryProvider.getAllDiaries(1, pageable)).willReturn(createTestPage(listOfDiaries, pageable));

        //when
        Page<FoodDiaryDTO> result = diaryService.getAllDiaries(1, pageable);

        //then
        assertAll(
                () -> assertThat(result.getContent().get(0), is(listOfDiaries.get(0))),
                () -> assertThat(result.getContent().get(1), is(listOfDiaries.get(1))),
                () -> assertThat(result.getContent().get(2), is(listOfDiaries.get(2)))
        );

    }

    @Test
    @DisplayName("Check if current diary for given user is returned")
    void shouldFindCurrentDiaryForUser() {
        //given
        LocalDate currentDate = LocalDate.now();
        given(diaryProvider.getDiaryByDate(1, currentDate)).willReturn(foodDiary1);

        //when
        FoodDiaryDTO currentDiary = diaryService.getCurrentDiary(1);

        //then
        assertThat(currentDiary, sameInstance(foodDiary1));
    }

    @Test
    @DisplayName("Check if diary of chosen date for given user is returned")
    void shouldFindDiaryForUserByDate() {
        //given
        String sDate = "2020-08-20";
        LocalDate date = LocalDate.parse(sDate);
        foodDiary2.setDate(date);
        given(diaryProvider.getDiaryByDate(1, date)).willReturn(foodDiary2);

        //when
        FoodDiaryDTO diary = diaryService.getDiaryByDate(1, "2020-08-20");

        //then
        assertThat(diary, sameInstance(foodDiary2));
    }

    @Test
    @DisplayName("Check if new diary for current date is created")
    void shouldCreateNewDiary() {
        //given
        LocalDate currentDate = LocalDate.now();
        FoodDiaryDTO newDiary =
                new FoodDiaryDTO(currentDate, Collections.emptyList()
                    ,new MealMacronutrients(0,0,0),
                0, 0, user1);
                        newDiary.setId(1L);
        given(diaryProvider.createDiary(currentDate,user1)).willReturn(newDiary);
        given(userService.getUserById(1)).willReturn(user1);

        //when
        FoodDiaryDTO diary = diaryService.createDiary(1);

        //then
        assertAll(
                () -> assertThat(diary.getListOfMeals().size(), equalTo(0)),
                () -> assertThat(diary.getTotalCalories(), equalTo(0)),
                () -> assertThat(diary.getTotalCalories(), equalTo(0))
        );
    }

    @Test
    @DisplayName("Check if true when diary exists")
    void shouldReturnTrueIfDiaryExists(){
        //given + when
        LocalDate currentDate = LocalDate.now();
        given(diaryProvider.existsByDate(1, currentDate)).willReturn(true);

        //then
        assertTrue(diaryService.existsByDate(1, currentDate));
    }

    @Test
    @DisplayName("Check if true when diary does not exist")
    void shouldReturnFalseIfDiaryExists(){
        //given + when
        LocalDate currentDate = LocalDate.now();
        given(diaryProvider.existsByDate(1, currentDate)).willReturn(false);

        //then
        assertFalse(diaryService.existsByDate(1, currentDate));
    }

    @Test
    @DisplayName("Check if exception is thrown when trying to create more than one diary per day")
    void shouldThrowEntityAlreadyFoundExceptionWhenCreatingMoreThanOneDiaryInOneDay(){
        //given + when
        given(diaryService.existsByDate(1, LocalDate.now())).willReturn(true);

        //then
        assertThrows(EntityAlreadyFoundException.class, () ->
                diaryService.createDiary(1));
    }

    @Test
    @DisplayName("Check if macronutrients are recalculated")
    void shouldRecalculateMacronutrientsAccordingToMeals() {
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
    @DisplayName("Check if price is recalculated")
    void shouldRecalculatePriceAccordingToMeals() {
        //given
        double totalPriceBefore = foodDiary1.getTotalPrice();
        foodDiary1.getListOfMeals().get(0).setPrice(7);

        //when
        diaryService.recalculatePrice(foodDiary1);

        //then
        assertThat(totalPriceBefore, is(foodDiary1.getTotalPrice()));
    }

    @Test
    @DisplayName("Check if total calories are recalculated")
    void shouldRecalculateTotalCaloriesAccordingToMeals() {
        //given
        double totalPriceBefore = foodDiary1.getTotalCalories();
        foodDiary1.setTotalCalories(0);

        //when
        diaryService.recalculatePrice(foodDiary1);

        //then
        assertThat(totalPriceBefore, is(not(0)));
    }

    @Test
    @DisplayName("Check if meal is added to diary")
    void shouldAddMeal() {
        //given
        LocalDate currentDate = LocalDate.now();
        given(mealService.getMealByName("Chicken and Strawberries")).willReturn(meal3);
        given(diaryProvider.getDiaryByDate(1, currentDate)).willReturn(foodDiary1);
        FoodDiaryDTO foodDiary4 =
                new FoodDiaryDTO(currentDate, List.of(meal1, meal2, meal3)
                        , new MealMacronutrients(0, 0, 0), 0, 0, user1);
        given(diaryProvider.updateDiary(foodDiary1)).willReturn(foodDiary4);
        //when
        FoodDiaryDTO diary4 = diaryService.addMeal(1, "Chicken and Strawberries");

        //then
        assertAll(
                () -> assertThat(diary4.getListOfMeals().size(), equalTo(foodDiary4.getListOfMeals().size())),
                () -> assertThat(diary4.getListOfMeals(), hasItem(meal3))
        );

    }

    @Test
    @DisplayName("Check if meal is deleted from diary")
    void shouldDeleteMeal() {
        //given
        LocalDate currentDate = LocalDate.now();
        List<MealDTO> listOfAll = new ArrayList<>();
        listOfAll.add(meal1);
        listOfAll.add(meal2);
        listOfAll.add(meal3);
        FoodDiaryDTO foodDiary4 = new FoodDiaryDTO(LocalDate.now(), listOfAll
                , new MealMacronutrients(150, 150, 150), 150, 100.0, user1);
        given(mealService.getMealByName("Chicken and Strawberries")).willReturn(meal3);
        given(diaryProvider.updateDiary(foodDiary4)).willReturn(foodDiary1);
        given(diaryProvider.getDiaryByDate(1, currentDate)).willReturn(foodDiary4);

        //when
        FoodDiaryDTO diary4 = diaryService.deleteMeal(1, "Chicken and Strawberries");

        //then
        assertAll(
                () -> assertThat(diary4.getListOfMeals().size(), equalTo(foodDiary1.getListOfMeals().size())),
                () -> assertThat(diary4.getListOfMeals(), not(hasItem(meal3)))
        );
    }

    @Test
    @DisplayName("Check if names of meals from three days before are rejected for algorithm")
    void shouldReturnNamesOfMealsUsedThreeDaysBack() {
        //given
        LocalDate threeDaysBack = LocalDate.now().minusDays(3);
        given(diaryProvider.rejectMealsFromThreeDaysBack(1, threeDaysBack)).willReturn(List.of(foodDiary1, foodDiary3));

        //when
        List<String> listOfNames = diaryService.rejectMealsFromThreeDaysBack(1);

        //then
        assertAll(
                () -> assertThat(listOfNames.size(), equalTo(4)),
                () -> assertThat(listOfNames, hasItem("Rice and Strawberry"))
        );
    }

    @Test
    @DisplayName("Check if exception is thrown when amount of calories for generator is less than 0")
    void shouldThrowIllegalArgumentExceptionIfAmountOfCaloriesIsLessThanZero() {
        //given + when
        int totalCalories = -5;
        int numberOfMeals = 5;

        // then
        assertThrows(IllegalArgumentException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, 1));
    }

    @Test
    @DisplayName("Check if exception is thrown when amount of calories for generator is greater than 10000")
    void shouldThrowIllegalArgumentExceptionIfAmountOfCaloriesIsGreaterThanTenThousands() {
        //given + when
        int totalCalories = 100001;
        int numberOfMeals = 5;

        // then
        assertThrows(IllegalArgumentException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, 1));
    }

    @Test
    @DisplayName("Check if exception is thrown when number of meals for generator is less than 3")
    void shouldThrowIllegalArgumentExceptionIfNumberOfMealsIsLessThanThree() {
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 2;

        // then
        assertThrows(IllegalArgumentException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, 1));
    }

    @Test
    @DisplayName("Check if exception is thrown when number of meals for generator is greater than 3")
    void shouldThrowIllegalArgumentExceptionIfNumberOfMealsIsGreaterThan7() {
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 10;

        // then
        assertThrows(IllegalArgumentException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, 1));
    }

    @Test
    @DisplayName("Check if exception is thrown when trying to generate by algorithm for non-empty diary")
    void shouldThrowIllegalArgumentExceptionIfAnyMealsExistInCurrentDiary() {
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 5;

        // then
        assertThrows(IllegalArgumentException.class, () ->
                diaryService.generateDiet(totalCalories, numberOfMeals, 1));
    }

    @Test
    @DisplayName("Check if exception is thrown when meals for generator is less than necessary")
    void shouldThrowRequiredMealsNotFoundExceptionIfListOfMealsForGeneratorIsTooSmall() {
        //given + when
        int totalCalories = 2500;
        int numberOfMeals = 5;
        given(diaryProvider.getDiaryByDate(1, LocalDate.now())).willReturn(foodDiary1);
        foodDiary1.setListOfMeals(Collections.emptyList());

        // then
        assertThrows(RequiredMealsNotFoundException.class, () ->
                diaryService.generateDiet(numberOfMeals, totalCalories, 1));
    }

    @Test
    @DisplayName("Check if 500 calories is subtracted when lose 0,5kg was chosen")
    void shouldSubtract500CaloriesIfHalfKgLossIsSelected(){
        //given
        int totalCalories = 1000;

        //when
        int result = diaryService.calculateTotalCaloriesAccordingToWeightGoal
                (WeightGoal.LOSEHALFKGPERWEEK, totalCalories);

        //then
        assertThat(result, equalTo(totalCalories - 500));
    }

    @Test
    @DisplayName("Check if 250 calories is subtracted when lose 0,25kg was chosen")
    void shouldSubtract250CaloriesIfQuarterKgLossIsSelected(){
        //given
        int totalCalories = 1000;

        //when
        int result = diaryService.calculateTotalCaloriesAccordingToWeightGoal
                (WeightGoal.LOSEQUARTERKGPERWEEK, totalCalories);

        //then
        assertThat(result, equalTo(totalCalories - 250));
    }

    @Test
    @DisplayName("Check if calories is the same when wieght maintenance was chosen")
    void shouldLeftInitialCaloriesIfMaintainIsSelected(){
        //given
        int totalCalories = 1000;

        //when
        int result = diaryService.calculateTotalCaloriesAccordingToWeightGoal
                (WeightGoal.MAINTAIN, totalCalories);

        //then
        assertThat(result, equalTo(totalCalories));
    }

    @Test
    @DisplayName("Check if 250 calories is added when gain 0,25kg was chosen")
    void shouldAdd250CaloriesIfQuarterKgGainIsSelected(){
        //given
        int totalCalories = 1000;

        //when
        int result = diaryService.calculateTotalCaloriesAccordingToWeightGoal
                (WeightGoal.GAINQUARTERKGPERWEEK, totalCalories);

        //then
        assertThat(result, equalTo(totalCalories + 250));
    }

    @Test
    @DisplayName("Check if 500 calories is added when gain 0,5kg was chosen")
    void shouldAdd500CaloriesIfHalfKgGainIsSelected(){
        //given
        int totalCalories = 1000;

        //when
        int result = diaryService.calculateTotalCaloriesAccordingToWeightGoal
                (WeightGoal.GAINHALFKGPERWEEK, totalCalories);

        //then
        assertThat(result, equalTo(totalCalories + 500));
    }


    @Test
    @DisplayName("Check if mismatched meals are discarded for further algorithm operations")
    void shouldDiscardMismatchedMeals() {
        //given
        List<String> matchingMeals = new ArrayList<>();
        matchingMeals.add("Rice and Chicken");
        matchingMeals.add("Scrambled Eggs");
        matchingMeals.add("Rice and Strawberry");
        matchingMeals.add("Hamburger");
        given(mealService.findNamesOfMatchingMeals(500)).willReturn(matchingMeals);
        given(diaryProvider.rejectMealsFromThreeDaysBack(1, LocalDate.now().minusDays(3))).willReturn(Collections.singletonList(foodDiary1));
        //when
        List<String> result = diaryService.discardMismatchedMeals(500, 1);

        //then
        assertAll(
                () -> assertThat(result, not(hasItem("Rice and Strawberry"))),
                () -> assertThat(result, not(hasItem("Rice and Chicken")))
        );
    }
}