package codebuddies.MealooApp.entities.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

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
    void shouldCalculateCorrectBMI() {
        //given + when
        double bmi = mealooUserDetails.calculateBMI();
        //then
        assertThat(bmi > 15);
        assertThat(bmi < 40);
    }

    @Test
    void calculateCaloricDemand() {
        //given + when
        int caloricDemand = mealooUserDetails.calculateCaloricDemand();
        //then
        assertThat(caloricDemand > 2000);
        assertThat(caloricDemand > 3500);
    }
}