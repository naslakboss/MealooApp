package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.ProductProvider;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductService {

    ProductProvider productProvider;

    public ProductService(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productProvider.getAllProducts(pageable);
    }

    public boolean existsByName(String name) {
        return productProvider.existsByName(name);
    }

    public ProductDTO getProductByName(String name) {
        return productProvider.getProductByName(name);
    }

    public ProductForIngredientDTO getProductForIngredientByName(String name) {
        return productProvider.getProductForIngredientByName(name);
    }

    public ProductDTO createProduct(ProductDTO product) {
        if (existsByName(product.getName())) {
            throw new EntityAlreadyFoundException(product.getName());
        }
        calculateCaloriesPer100g(product);
        return productProvider.createProduct(product);
    }

    public void calculateCaloriesPer100g(ProductDTO product) {
        Macronutrients macronutrients = product.getMacronutrients();

        int result =
                  macronutrients.getProteinsPer100g() * 4
                + macronutrients.getCarbohydratesPer100g() * 4
                + macronutrients.getFatsPer100g() * 9;

        product.setCaloriesPer100g(result);
    }

    public ProductDTO updateProductByName(ProductDTO product, String name) {
        product.setName(name);
        return productProvider.updateProduct(product);
    }

    @Transactional
    public void deleteProductByName(String name) {
        if (!existsByName(name)) {
            throw new ResourceNotFoundException(name);
        }
        productProvider.deleteByName(name);
    }

}
