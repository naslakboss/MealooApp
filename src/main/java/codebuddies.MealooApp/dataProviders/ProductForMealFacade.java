package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductForMealFacade {

    ProductService productService;
    ModelMapper modelMapper;


    public ProductForMealFacade(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    public ProductForMealDTO getProductByName(String name){
        return modelMapper.map(productService.findByName(name), ProductForMealDTO.class);
    }


    public List<ProductForMealDTO> getAllProducts(){


        List<ProductForMealDTO> listOfProducts = productService.findAll()
                .stream().map(product -> modelMapper.map(product, ProductForMealDTO.class))
                .collect(Collectors.toList());
        return listOfProducts;

    }
}
