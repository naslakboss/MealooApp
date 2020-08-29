package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.exceptions.ValidationException;
import codebuddies.MealooApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {


    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) throws ValidationException {
        checkTheCorrectnessOfQuantity(product);
        product.setCaloriesPer100g(product.calculateCaloriesPer100g());
        return productRepository.save(product);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public  Product findByName(String name) throws ResourceNotFoundException {
        Product product = productRepository.findByName(name);
        if(product == null){
            throw new ResourceNotFoundException(name);
        }
        return product;
    }

    @Transactional
    public  Product updateByName(String name, Product product) throws ResourceNotFoundException, ValidationException {
        Product foundedProduct = findByName(name);
        Macronutrients macro = new Macronutrients();
        Macronutrients newMacro = product.getMacronutrients();

        if(product.getName() != null){
            foundedProduct.setName(product.getName());
        }
        if(product.getPrice() != 0){
            foundedProduct.setPrice(product.getPrice());
        }
        if(newMacro.getProteinsPer100g()!= 0){
            macro.setCarbohydratesPer100g(newMacro.getProteinsPer100g());
        }
        if(newMacro.getFatsPer100g()!= 0){
            macro.setFatsPer100g(newMacro.getCarbohydratesPer100g());
        }
        if(product.getCaloriesPer100g() != 0){
            foundedProduct.setCaloriesPer100g(product.getCaloriesPer100g());
        }
        if(product.getProductType()!=null){
            foundedProduct.setProductType(product.getProductType());
        }
        foundedProduct.setMacronutrients(newMacro);
        foundedProduct.calculateCaloriesPer100g();
        save(foundedProduct);
        return foundedProduct;
    }


    public void deleteByName(String name) throws ResourceNotFoundException {
        if(!existsByName(name)){
            throw new ResourceNotFoundException("This product cannot be deleted because it does not exist");
        }
        productRepository.deleteByName(name);
    }

    public boolean checkTheCorrectnessOfQuantity(Product product) throws ValidationException {
        if(product.getMacronutrients().getProteinsPer100g() + product.getMacronutrients().getCarbohydratesPer100g() > 100
                || product.getMacronutrients().getProteinsPer100g() + product.getMacronutrients().getFatsPer100g() > 100
                    || product.getMacronutrients().getCarbohydratesPer100g() + product.getMacronutrients().getFatsPer100g() > 100
                        || product.getMacronutrients().getProteinsPer100g() + product.getMacronutrients().getCarbohydratesPer100g()
                            + product.getMacronutrients().getFatsPer100g() > 100){
            throw new ValidationException("Total sum of Macronutrients in 100g of product" +
                    " cannot exceed 100g");
        }
        return true;
    }
}
