package codebuddies.MealooApp.datamappers;

import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.dto.ProductForIngredientDTO;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.ProductRepository;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductProvider {

    ModelMapper modelMapper = new ModelMapper();

    ProductRepository productRepository;

    public ProductProvider(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public ProductDTO getProductByName(String name) {
        Product product = productRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException(name));

        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductForIngredientDTO getProductForIngredientByName(String name) {
        Product product = productRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException(name));

        return modelMapper.map(product, ProductForIngredientDTO.class);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        productRepository.save(product);

        return productDTO;
    }

    public ProductDTO updateProduct(ProductDTO updatedProduct) {
        Product product = modelMapper.map(updatedProduct, Product.class);
        productRepository.save(product);

        return updatedProduct;
    }

    public void deleteByName(String name) {
        if(!existsByName(name)){
            throw new ResourceNotFoundException(name);
        }
        productRepository.deleteByName(name);
    }

}
