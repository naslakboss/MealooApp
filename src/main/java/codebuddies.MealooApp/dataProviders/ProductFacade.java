package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.services.ProductService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductFacade {

    ProductService productService;
    ModelMapper modelMapper;


    public ProductFacade(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    public ProductDTO getProductByName(String name){
        return modelMapper.map(productService.findByName(name), ProductDTO.class);
    }
    

    public List<ProductDTO> getAllProducts(){
       

        List<ProductDTO> listOfProducts = productService.findAll()
                .stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return listOfProducts;

    }
}
