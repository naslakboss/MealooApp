package codebuddies.MealooApp.services;

//import codebuddies.MealooApp.entities.ProductType;
//import codebuddies.MealooApp.repositories.ProductTypeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//
//@Service
//public class ProductTypeService {
//
//    @Autowired
//    ProductTypeRepository productTypeRepository;
//
//    public List<ProductType> findAll() {
//        return productTypeRepository.findAll();
//    }
//
//    public void deleteByName(String name) {
//        productTypeRepository.deleteByName(name);
//    }
//
//    public boolean existsByName(String name) {
//        return productTypeRepository.existsByName(name);
//    }
//
//    public ProductType save(ProductType dairy) {
//        return productTypeRepository.save(dairy);
//    }
//
//    public ProductType findByName(String name) {
//        return productTypeRepository.findByName(name).get();
//    }
//
//    public ProductType patchByName(String name, ProductType productType) {
//        ProductType patchedProductType = productTypeRepository.findByName(name).get();
//        if(productType.getDescription() != null){
//            patchedProductType.setDescription(productType.getDescription());
//        }
//        return patchedProductType;
//    }
//}
