package codebuddies.MealooApp.entities.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class MealooUserDetailsTest {

    MealooUser mealooUser;
    MealooUserDetails mealooUserDetails;

    @BeforeEach
    void setUp(){
        mealooUserDetails = new MealooUserDetails(190, 80, 30, Sex.MALE, PhysicalActivity.HUGE);
        mealooUser = new MealooUser(007L, "James", "007", "secretBondmail@gmail.com"
                , new NutritionSettings(3007), mealooUserDetails);

    }
    @Test
    void shouldReturnCalculatedBMIWhenDataIsCorrect() {
        //given
        //when
        // Co powinienem tutaj sprawdzic? Przepisać wzroc czy przeliczyc wlasnorecznie?
        //then
        assertThat(mealooUserDetails.calculateBMI(), equalTo(22.0));
    }

    @Test
    void calculateCaloricDemand() {
        //given
        //when
        //then
        assertThat(mealooUserDetails.calculateCaloricDemand(), equalTo(3912));
    }
}