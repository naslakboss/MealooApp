package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.ProductProvider;
import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.exceptions.ValidationException;
import codebuddies.MealooApp.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductService {


    ProductRepository productRepository;

    ProductProvider productProvider;

    public ProductService(ProductRepository productRepository, ProductProvider productProvider) {
        this.productRepository = productRepository;
        this.productProvider = productProvider;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productProvider.getAllProducts(pageable);
    }

    public ProductDTO getProductByName(String name) {
        return productProvider.getProductByName(name);
    }

    public ProductDTO createProduct(ProductDTO productDTO) throws ValidationException {
        checkTheCorrectnessOfQuantity(productDTO);
        calculateCaloriesPer100g(productDTO);
        return productProvider.createProduct(productDTO);
    }

    public boolean checkTheCorrectnessOfQuantity(ProductDTO productDTO) throws ValidationException {
        if(productDTO.getMacronutrients().getProteinsPer100g() + productDTO.getMacronutrients().getCarbohydratesPer100g() > 100
                || productDTO.getMacronutrients().getProteinsPer100g() + productDTO.getMacronutrients().getFatsPer100g() > 100
                || productDTO.getMacronutrients().getCarbohydratesPer100g() + productDTO.getMacronutrients().getFatsPer100g() > 100
                || productDTO.getMacronutrients().getProteinsPer100g() + productDTO.getMacronutrients().getCarbohydratesPer100g()
                + productDTO.getMacronutrients().getFatsPer100g() > 100){
            throw new ValidationException("Total sum of Macronutrients in 100g of product" +
                    " cannot exceed 100g");
        }
        return true;
    }

    public void calculateCaloriesPer100g(ProductDTO productDTO){
        Macronutrients macronutrients = productDTO.getMacronutrients();
        int result = macronutrients.getProteinsPer100g() * 4 + macronutrients.getCarbohydratesPer100g() * 4
                + macronutrients.getFatsPer100g() * 9;
        productDTO.setCaloriesPer100g(result);
    }

    public ProductDTO updateProductByName(ProductDTO productDTO, String name) {
        ProductDTO updatedProduct = getProductByName(name);
        updatedProduct.setName(productDTO.getName());
        updatedProduct.setPrice(productDTO.getPrice());
        updatedProduct.setCaloriesPer100g(calculateCaloriesPer100g(productDTO););
        updatedProduct.getMacronutrients().setProteinsPer100g(productDTO.getMacronutrients().getProteinsPer100g());
        updatedProduct.getMacronutrients().setCarbohydratesPer100g(productDTO.getMacronutrients().getCarbohydratesPer100g());
        updatedProduct.getMacronutrients().setFatsPer100g(productDTO.getMacronutrients().getFatsPer100g());
        updatedProduct.setProductType(productDTO.getProductType());

        return productProvider.updateProduct(productDTO);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Transactional
    public void deleteProductByName(String name) throws ResourceNotFoundException {
        if(!existsByName(name)){
            throw new ResourceNotFoundException("This product cannot be deleted because it does not exist");
        }
        else productRepository.deleteByName(name);
    }


}
