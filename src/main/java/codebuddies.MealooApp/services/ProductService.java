package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.ProductProvider;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.exceptions.ValidationException;
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

    public ProductDTO getProductByName(String name) {
        return productProvider.getProductByName(name);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        if(existsByName(productDTO.getName())){
            throw new EntityAlreadyFoundException(productDTO.getName());
        }
        calculateCaloriesPer100g(productDTO);
        return productProvider.createProduct(productDTO);
    }

    private boolean existsByName(String name) {
        return productProvider.existsByName(name);
    }

    public void calculateCaloriesPer100g(ProductDTO productDTO){
        Macronutrients macronutrients = productDTO.getMacronutrients();
        int result = macronutrients.getProteinsPer100g() * 4 + macronutrients.getCarbohydratesPer100g() * 4
                + macronutrients.getFatsPer100g() * 9;
        productDTO.setCaloriesPer100g(result);
    }

    public ProductDTO updateProductByName(ProductDTO product, String name) {
        product.setName(name);
        return productProvider.updateProduct(product);
    }

    @Transactional
    public void deleteProductByName(String name) {
        productProvider.deleteByName(name);
    }


}
