package mealooapp.MealooApp.controllers;


//import codebuddies.MealooApp.controllers.MealController;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import codebuddies.MealooApp.entities.*;
//import codebuddies.MealooApp.repositories.MealRepository;
//import codebuddies.MealooApp.services.MealService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebMvcTest(MealController.class)
//@ExtendWith(SpringExtension.class)
//class MealControllerIntegrationTest {
//
//
//    private static ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private MealController mealController;
//
//    @MockBean
//    private MealService mealService;
//
//    @MockBean
//    private MealRepository mealRepository;
//
//
//    @Test
//    void shoultNotFindMealWhenNameIsIncorrect() throws Exception {
//        //given
//        ProductType dairy = new ProductType("DAIRY");
//        ProductType grains = new ProductType("GRAINS");
//
//        Product egg = new Product("Eggs", 1, 140,
//                new Macronutrients(13, 1, 10), dairy);
//        Product bread = new Product("Bread",3, 264,
//                new Macronutrients(9, 50, 3), grains);
//        Set<Product> listForScrambledEggs = new HashSet<>();
//        listForScrambledEggs.add(egg);
//        listForScrambledEggs.add(bread);
//        Meal meal = new Meal("Scrambled-eggs", listForScrambledEggs, 4, "EASY");
//        //when
//        when(mealService.findByName("Scrambled-eggs")).thenReturn(meal);
//        //then
//        mockMvc.perform(get("/meal/Oscypki")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(meal)))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    void shouldAddNewMealWithProperData() throws Exception {
//        //given
//        ProductType dairy = new ProductType("DAIRY");
//        ProductType grains = new ProductType("GRAINS");
//        Product egg = new Product("Eggs", 1, 140,
//                new Macronutrients(13, 1, 10), dairy);
//        Product bread = new Product("Bread",3, 264,
//                new Macronutrients(9, 50, 3), grains);
//        Set<Product> listForScrambledEggs = new HashSet<>();
//        listForScrambledEggs.add(egg);
//        listForScrambledEggs.add(bread);
//        Meal meal = new Meal("Scrambled-eggs", listForScrambledEggs, 4, "EASY");
//        mealService.save(meal);
//        //when
//        when(mealService.save(any(Meal.class))).thenReturn(meal);
//        //then
//        mockMvc.perform(MockMvcRequestBuilders.post("/meal/add")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(meal)))
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andExpect(jsonPath("$.mealDifficulty").value("EASY"))
//                .andExpect(jsonPath("$.price").value(4))
//                .andExpect(jsonPath("$.name").value("Scrambled-eggs"));
//
//    }
//
//    @Test
//    void shouldDeleteMealFromDatabaseWhenNameIsCorrect() throws Exception {
//        ProductType dairy = new ProductType("DAIRY");
//        ProductType grains = new ProductType("GRAINS");
//        ProductType meat = new ProductType("MEAT");
//
//        Product egg = new Product("Eggs", 1, 140,
//                new Macronutrients(13, 1, 10), dairy);
//        Product bread = new Product("Bread",3, 264,
//                new Macronutrients(9, 50, 3), grains);
//        Product rice = new Product("Rice", 3, 350,
//                new Macronutrients(1, 2, 3), grains);
//        Product chicken = new Product("Chicken",3, 130,
//                new Macronutrients(99, 50, 3), meat);
//        Set<Product> listOne = new HashSet<>();
//        listOne.add(egg);
//        listOne.add(bread);
//        Meal meal = new Meal("Scrambled-eggs", listOne, 4, "EASY");
//
//        Set<Product> listTwo = new HashSet<>();
//        listTwo.add(rice);
//        listTwo.add(chicken);
//        Meal meal2 = new Meal("Scrambled-eggs", listTwo, 4, "EASY");
//        mealService.save(meal);
//        mealService.save(meal2);
//        List<Meal> mealsList = Arrays.asList(meal, meal2);
//        //when
//        doNothing().when(mealService).deleteByName(meal.getName());
//        //
//        mockMvc.perform(get("/meal")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(mealsList)))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$[1].name").doesNotExist());
//    }
//
//
//}