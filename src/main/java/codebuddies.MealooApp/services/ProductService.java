package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public  Product findByName(String name) throws ResourceNotFoundException {
        Product product = productRepository.findByName(name);
        if(product == null){
            throw new ResourceNotFoundException("Product of given name does not exist in database");
        }
        return product;
    }

    public  Product updateByName(String name, Product product) throws ResourceNotFoundException {
        Product foundedProduct = findByName(name);
        Macronutrients macro = new Macronutrients();
        Macronutrients newMacro = product.getMacronutrients();
        if(product.getPrice() != 0){
            foundedProduct.setPrice(product.getPrice());
        }
        if(product.getCaloriesPer100g() != 0){
            foundedProduct.setCaloriesPer100g(product.getCaloriesPer100g());
        }
        if(product.getProductType()!=null){
            foundedProduct.setProductType(product.getProductType());
        }
        if(newMacro.getCarbohydratesPer100g()!= 0){
            macro.setCarbohydratesPer100g(newMacro.getCarbohydratesPer100g());
        }
        if(newMacro.getProteinsPer100g()!= 0){
            macro.setCarbohydratesPer100g(newMacro.getProteinsPer100g());
        }
        if(newMacro.getFatsPer100g()!= 0){
            macro.setFatsPer100g(newMacro.getCarbohydratesPer100g());
        }
        foundedProduct.setMacronutrients(newMacro);
        return foundedProduct;
    }


    public void deleteByName(String name) throws ResourceNotFoundException {
        if(!existsByName(name)){
            throw new ResourceNotFoundException("This product cannot be deleted because it does not exist");
        }
        productRepository.deleteByName(name);
    }
}
