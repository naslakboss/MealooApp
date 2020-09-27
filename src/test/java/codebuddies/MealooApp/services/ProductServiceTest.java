package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.ProductProvider;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Mock
    ProductProvider productProvider;

    ProductDTO product1;
    ProductDTO product2;
    ProductDTO product3;

    List<ProductDTO> products;

    ProductService productService;


    @BeforeEach
    void setUp(){
        product1 = new ProductDTO("Potato", 5,  75, new Macronutrients(2, 17,0),  ProductType.GRAINS);
        product2 = new ProductDTO("Beef", 30, 250, new Macronutrients(26, 0, 15), ProductType.MEAT);
        product3 = new ProductDTO("Chicken", 12, 121, new Macronutrients(22, 1, 4), ProductType.MEAT);

        products = List.of(product1, product2, product3);

        productService = new ProductService(productProvider);
    }

    Page<ProductDTO> createTestPage(Pageable pageable){
        List<ProductDTO> listOfProducts = products;
        return new PageImpl<>(listOfProducts, pageable, listOfProducts.size());
    }

    @Test
    void findAllShouldReturnListOfProduct() {
        //given
        Pageable pageable = PageRequest.of(0, 3);
        when(productProvider.getAllProducts(pageable)).thenReturn(createTestPage(pageable));

        //when
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        List<ProductDTO> productsList = products.getContent();

        //then
        assertAll(
                () -> assertThat(productsList.size(), equalTo(3)),
                () -> assertThat(productsList.get(0).getName(), equalTo("Potato")),
                () -> assertThat(productsList.get(1).getPrice(), equalTo(30.0)),
                () -> assertThat(productsList.get(2).getMacronutrients().getProteinsPer100g(), equalTo(22))
        );
    }

    @Test
    void findByNameShouldReturnProductIfNameIsCorrect(){
        //given
        ProductType grains = ProductType.GRAINS;
        when(productProvider.getProductByName("Potato")).thenReturn(product1);

        //when
        ProductDTO potato = productService.getProductByName("Potato");

        //then
        assertAll(
                () -> assertThat(potato.getName(), equalTo("Potato")),
                () -> assertThat(potato.getPrice(), equalTo(5.0)),
                () -> assertThat(potato.getProductType(), equalTo(grains))
        );
    }

    @Test
    void findByNameShouldThrowsAnResourceNotFoundExceptionProductIsNotExists(){
        //given + when
        when(productProvider.getProductByName("BadName")).thenThrow(ResourceNotFoundException.class);

        //then
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductByName("BadName"));
    }

    @Test
    void saveProductTestOK() {
        //given
        when(productProvider.createProduct(product3)).thenReturn(product3);

        //when
        ProductDTO product = productService.createProduct(product3);

        //then
        assertAll(
                () -> assertThat(product.getName(), equalTo("Chicken")),
                () -> assertThat(product.getPrice(), equalTo(12.0)),
                () -> assertThat(product.getCaloriesPer100g(), greaterThan(0))
        );
    }

    @Test
    void shouldReturnTrueIfProductExists(){
        //given + when
        when(productProvider.existsByName(anyString())).thenReturn(true);

        //then
        assertTrue(productService.existsByName("Lettuce"));
    }

    @Test
    void shouldReturnFalseIfProductNotExists(){
        //given + when
        when(productProvider.existsByName(anyString())).thenReturn(false);

        //then
        assertFalse(productService.existsByName("Hamburger"));
    }



    @Test
    void shouldThrowAnExceptionWhenProductToUpdateDoesNotExist() {
        //given + when
        when(productProvider.getProductByName(anyString())).thenThrow(ResourceNotFoundException.class);

        //then
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductByName("FrenchFries"));
    }

    @Test
    void shouldUpdateProductDataWhenProductExistsAndNewDataFormatIsCorrect() {
        //given
        ProductDTO newData = new ProductDTO("ChickenTenderloin", 18, 90, new Macronutrients(17, 1, 2), ProductType.MEAT);
        when(productProvider.updateProduct(newData)).thenReturn(newData);

        //when
        ProductDTO productAfterUpdate = productService.updateProductByName(newData, "Chicken");

        //then
        assertAll(
                () -> assertThat(productAfterUpdate.getName(), equalTo("Chicken")),
                () -> assertThat(productAfterUpdate.getPrice(), equalTo(18.0)),
                () -> assertThat(productAfterUpdate.getMacronutrients().getProteinsPer100g(), equalTo(17)),
                () -> assertThat(productAfterUpdate.getMacronutrients().getCarbohydratesPer100g(), equalTo(1)),
                () -> assertThat(productAfterUpdate.getMacronutrients().getFatsPer100g(), equalTo(2)),
                () -> assertThat(productAfterUpdate.getProductType(), equalTo(ProductType.MEAT))
        );
    }
    @Test
    void shouldDeleteProductWhenProductExists() {
        //given
        doNothing().when(productProvider).deleteByName("Chicken");

        //when
        productService.deleteProductByName("Chicken");

        //then
        verify(productProvider, times(1)).deleteByName("Chicken");
    }

}