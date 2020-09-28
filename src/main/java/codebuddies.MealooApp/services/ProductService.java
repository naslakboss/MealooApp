package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.ProductMapper;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductService {

    ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productMapper.getAllProducts(pageable);
    }

    public boolean existsByName(String name) {
        return productMapper.existsByName(name);
    }

    public ProductDTO getProductByName(String name) {
        return productMapper.getProductByName(name);
    }

    public ProductForIngredientDTO getProductForIngredientByName(String name) {
        return productMapper.getProductForIngredientByName(name);
    }

    public ProductDTO createProduct(ProductDTO product) {
        if (existsByName(product.getName())) {
            throw new EntityAlreadyFoundException(product.getName());
        }
        calculateCaloriesPer100g(product);
        return productMapper.createProduct(product);
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
        return productMapper.updateProduct(product);
    }

    @Transactional
    public void deleteProductByName(String name) {
        productMapper.deleteByName(name);
    }

}
