package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductForIngredientFacade {

    ProductService productService;
    ModelMapper modelMapper;


    public ProductForIngredientFacade(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    public ProductForIngredientDTO getProductByName(String name){
        return modelMapper.map(productService.findByName(name), ProductForIngredientDTO.class);
    }
    public List<ProductForIngredientDTO> getAllProducts(){
        List<ProductForIngredientDTO> listOfProducts = productService.findAll()
                .stream().map(product -> modelMapper.map(product, ProductForIngredientDTO.class))
                .collect(Collectors.toList());
        return listOfProducts;

    }
}