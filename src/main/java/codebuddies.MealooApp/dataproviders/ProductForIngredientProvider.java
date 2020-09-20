package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductForIngredientProvider {

    ProductService productService;
    ModelMapper modelMapper;


    public ProductForIngredientProvider(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    public ProductForIngredientDTO getProductByName(String name) throws ResourceNotFoundException {
        return modelMapper.map(productService.getProductByName(name), ProductForIngredientDTO.class);
    }
//    public List<ProductForIngredientDTO> getAllProducts(Pageable pageable){
//        List<ProductForIngredientDTO> listOfProducts = productService.findAll(pageable)
//                .stream().map(product -> modelMapper.map(product, ProductForIngredientDTO.class))
//                .collect(Collectors.toList());
//        return listOfProducts;

//    }
}
