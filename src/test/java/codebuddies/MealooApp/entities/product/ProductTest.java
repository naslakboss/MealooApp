package codebuddies.MealooApp.entities.product;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(Product.class)
class ProductTest {

    @Test
    void shouldReturnProperValueIfMacronutrientsHaveGoodFormat() {
        //given
        Macronutrients macro = new Macronutrients(2, 17,0);
        Product product = new Product("Potato", 3, macro, ProductType.GRAINS);
        //when
        //then
        assertThat(product.getCaloriesPer100g(), equalTo(76));
    }


}