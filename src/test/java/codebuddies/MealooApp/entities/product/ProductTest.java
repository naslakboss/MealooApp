package codebuddies.MealooApp.entities.product;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.equalTo;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class ProductTest {

    Product product;
    Macronutrients macronutrients;
    @Test
    void shouldReturnProperValueIfMacronutrientsHaveGoodFormat() {
        //given
         macronutrients = new Macronutrients(2, 17,0);
//         product = new Product("Potato", 3, macronutrients, ProductType.GRAINS);
        //when
        //then
        assertThat(product.getCaloriesPer100g(), equalTo(76));
    }


}