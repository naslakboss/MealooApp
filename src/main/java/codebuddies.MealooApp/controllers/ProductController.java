package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.exceptions.ValidationException;
import codebuddies.MealooApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/products")
//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
public class ProductController {

    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String name){
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) throws ValidationException {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{name}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody @Valid ProductDTO productDTO,@PathVariable String name) throws ValidationException {
        return  ResponseEntity.ok(productService.updateProductByName(productDTO, name));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity deleteProduct(@PathVariable String name) throws ResourceNotFoundException {
        productService.deleteProductByName(name);
        return ResponseEntity.ok("Product " + name + " was successfully deleted from Repository");
    }
}
