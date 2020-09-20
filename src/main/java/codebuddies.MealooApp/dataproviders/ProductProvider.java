package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.ProductRepository;
import codebuddies.MealooApp.services.ProductService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductProvider {

    ModelMapper modelMapper;
    ProductRepository productRepository;


    public ProductProvider(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable).
                map(products -> modelMapper.map(products, ProductDTO.class));

    }

    public ProductDTO getProductByName(String name) {
        Product product = productRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Product " + name + " does not exist"));
        return modelMapper.map(product, ProductDTO.class);

    }

    public ProductDTO createProduct(ProductDTO productDTO){
        Product product = modelMapper.map(productDTO, Product.class);
        productRepository.save(product);
        return productDTO;
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
}
