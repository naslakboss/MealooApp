package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
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

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public  Product findByName(String name) {
        return productRepository.findByName(name);
    }

    public  Product updateByName(String name, Product product) {
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

    public Product save(Product product) {
            return productRepository.save(product);
        }

}
