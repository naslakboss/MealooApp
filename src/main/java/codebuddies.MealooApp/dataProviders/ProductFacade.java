package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.ProductService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
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

    public ProductDTO getProductByName(String name) throws ResourceNotFoundException {
        if(!productService.existsByName(name)){
            throw new ResourceNotFoundException("Product of given name does not exist in database");
        }
        return modelMapper.map(productService.findByName(name), ProductDTO.class);
    }
    

    public List<ProductDTO> getAllProducts(Pageable pageable){


        return productService.findAll(pageable)
                .stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

    }

    public boolean existsByName(String name) {
        return productService.existsByName(name);
    }
}
