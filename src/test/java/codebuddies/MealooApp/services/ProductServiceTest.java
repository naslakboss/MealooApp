package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.ProductProvider;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.exceptions.ValidationException;
import codebuddies.MealooApp.repositories.ProductRepository;

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

//@MockitoSettings(strictness = Strictness.STRICT_STUBS)
//@ExtendWith(SpringExtension.class)
//class ProductServiceTest {
//
//    @Mock
//    ProductRepository productRepository;
//
//    @Mock
//    ProductProvider productProvider;
//
//    Product product1;
//    Product product2;
//    Product product3;
//    List<Product> products;
//    ProductService productService;
//
//
//    @BeforeEach
//    void setUp(){
//        product1 = new Product("Potato", 5, new Macronutrients(2, 17,0), ProductType.GRAINS);
//        product2 = new Product("Beef", 30, new Macronutrients(26, 0, 15), ProductType.MEAT);
//        product3 = new Product("Chicken", 12, new Macronutrients(22, 1, 4), ProductType.MEAT);
//        products = List.of(product1, product2, product3);
//        productService = new ProductService(productRepository, productProvider);
//    }
//    Page<Product> createTestPage(Pageable pageable){
//        List<Product> listOfProducts = products;
//        return new PageImpl<>(listOfProducts, pageable, listOfProducts.size());
//    }
//
//    @Test
//    void findAllShouldReturnListOfProduct() {
//        //given
//        Pageable pageable = PageRequest.of(0, 3);
//        when(productRepository.findAll(pageable)).thenReturn(createTestPage(pageable));
//        //when
//
//        Page<ProductDTO> products = productService.findAllProducts(pageable);
//        List<ProductDTO> productsList = products.getContent();
//        //then
//        assertAll(
//                () -> assertThat(productsList.size(), equalTo(3)),
//                () -> assertThat(productsList.get(0).getName(), equalTo("Potato")),
//                () -> assertThat(productsList.get(1).getPrice(), equalTo(30.0)),
//                () -> assertThat(productsList.get(2).getMacronutrients().getProteinsPer100g(), equalTo(22))
//        );
//    }
//
//    @Test
//    void findByNameShouldReturnProductIfNameIsCorrect(){
//        //given
//        ProductType grains = ProductType.GRAINS;
//        when(productRepository.findByName("Potato")).thenReturn(product1);
//        //when
//        Product potato = productService.findByName("Potato");
//        //then
//        assertAll(
//                () -> assertThat(potato.getName(), equalTo("Potato")),
//                () -> assertThat(potato.getPrice(), equalTo(5.0)),
//                () -> assertThat(potato.getProductType(), equalTo(grains))
//        );
//    }
//
//    @Test
//    void findByNameShouldThrowsAnResourceNotFoundExceptionProductIsNotExists(){
//        //given + when
//        when(productRepository.findByName("BadName")).thenThrow(ResourceNotFoundException.class);
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> productService.findByName("BadName"));
//    }
//
//    @Test
//    void saveProductTestOK() throws ValidationException {
//        //given
//        when(productRepository.save(product3)).thenReturn(product3);
//        //when
//        ProductDTO product = productService.save(product3);
//        //then
//        assertAll(
//                () -> assertThat(product.getName(), equalTo("Chicken")),
//                () -> assertThat(product.getPrice(), equalTo(12.0)),
//                () -> assertThat(product.getCaloriesPer100g(), greaterThan(0)),
//                () -> assertTrue(productService.checkTheCorrectnessOfQuantity(product))
//        );
//    }
//
//    @Test
//    void shouldThrowsAnExceptionWhenAmountOfMacronutrientsIsNotCorrect() throws ValidationException {
//        //given + when
//        Product badProduct = new Product("ScoobySnacks", 999
//                , new Macronutrients(100, 51, 51), ProductType.MEAT);
//        //then
//        assertThrows(ValidationException.class, () -> productService.checkTheCorrectnessOfQuantity(badProduct));
//    }
//
//    @Test
//    void shouldReturnTrueWhenAmountOfMacronutrientsIsCorrect() throws ValidationException {
//        //given + when
//        Product goodProduct = new Product("Fish" , 20
//                , new Macronutrients(20, 20, 20), ProductType.FISH);
//        //then
//        assertTrue(productService.checkTheCorrectnessOfQuantity(goodProduct));
//    }
//
//    @Test
//    void shouldReturnTrueIfProductExists(){
//        //given + when
//        when(productRepository.existsByName(anyString())).thenReturn(true);
//        //then
//        assertTrue(productService.existsByName("Lettuce"));
//    }
//
//    @Test
//    void shouldReturnFalseIfProductNotExists(){
//        //given + when
//        when(productRepository.existsByName(anyString())).thenReturn(false);
//        //then
//        assertFalse(productService.existsByName("Hamburger"));
//    }
//
//
//
//    @Test
//    void shouldThrowAnExceptionWhenProductToUpdateDoesNotExist() {
//        //given + when
//        when(productRepository.findByName(anyString())).thenThrow(ResourceNotFoundException.class);
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> productService.updateByName("FrenchFries", product1));
//    }
//
//    @Test
//    void shouldUpdateProductDataWhenProductExistsAndNewDataFormatIsCorrect() throws ValidationException {
//        //given
//        ProductType meat = ProductType.MEAT;
//        when(productRepository.findByName("Chicken")).thenReturn(product3);
//        Product newData = new Product("ChickenTenderloin", 18, new Macronutrients(17, 1, 2), ProductType.MEAT);
//        //when
//        Product productAfterUpdate = productService.updateByName("Chicken", newData);
//        //then
//        assertAll(
//                () -> assertThat(productAfterUpdate.getName(), equalTo("ChickenTenderloin")),
//                () -> assertThat(productAfterUpdate.getPrice(), equalTo(18.0)),
//                () -> assertThat(productAfterUpdate.getMacronutrients().getProteinsPer100g(), equalTo(17)),
//                () -> assertThat(productAfterUpdate.getMacronutrients().getCarbohydratesPer100g(), equalTo(1)),
//                () -> assertThat(productAfterUpdate.getMacronutrients().getFatsPer100g(), equalTo(2)),
//                () -> assertThat(productAfterUpdate.getProductType(), equalTo(meat))
//        );
//    }
//    @Test
//    void shouldDeleteProductWhenProductExists() {
//        //given
//        when(productRepository.existsByName("Chicken")).thenReturn(true);
//        doNothing().when(productRepository).deleteByName("Chicken");
//        //when
//        productService.deleteByName("Chicken");
//        //then
//        verify(productRepository, times(1)).deleteByName("Chicken");
//    }
//
//    @Test
//    void shouldThrowAnExceptionDuringRemovalWhenProductDoesNotExist(){
//        //given + when
//        when(productRepository.existsByName("BadName")).thenReturn(false);
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> productService.deleteByName("BadName"));
//    }
//}