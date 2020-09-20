package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dto.ProductDTO;
import codebuddies.MealooApp.dataproviders.ProductProvider;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.exceptions.ValidationException;
import codebuddies.MealooApp.services.ProductService;
//import mealoapp.MealooAppp.services.ProductTypeService;
//import codebuddies.MealooApp.services.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
public class ProductController {

    ProductService productService;

    ProductProvider productProvider;

    @Autowired
    public ProductController(ProductService productService, ProductProvider productProvider) {
        this.productService = productService;
        this.productProvider = productProvider;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB() throws ValidationException {
//
//        Product egg = new Product("Eggs", 10,
//                new Macronutrients(13, 1, 10), ProductType.DAIRY);
//        Product bread = new Product("Bread", 3,
//                new Macronutrients(9, 50, 3), ProductType.GRAINS);
//        Product milk = new Product("Milk", 4,
//                new Macronutrients(3, 4, 4), ProductType.DAIRY);
//        Product beef = new Product("Beef", 30,
//                new Macronutrients(30, 5, 30), ProductType.MEAT);
//        Product chicken = new Product("Chicken", 15,
//                new Macronutrients(22, 0, 1), ProductType.MEAT);
//        Product pasta = new Product("Pasta", 8,
//                new Macronutrients(12, 65, 2), ProductType.MEAT);
//        Product strawberry = new Product("Strawberry", 5,
//                new Macronutrients(2, 12, 2), ProductType.DAIRY);
//        Product piers = new Product("ChickenBreast", 2,
//                new Macronutrients(31, 0, 4), ProductType.MEAT);
//        Product rice = new Product("WhiteRice", 1,
//                new Macronutrients(7, 80, 1), ProductType.GRAINS);
//        Product paprika = new Product("Paprika", 3,
//                new Macronutrients(1, 7, 1), ProductType.VEGETABLES);
//        productService.save(egg);
//        productService.save(bread);
//        productService.save(milk);
//        productService.save(beef);
//        productService.save(chicken);
//        productService.save(pasta);
//        productService.save(strawberry);
//        productService.save(piers);
//        productService.save(rice);
//        productService.save(paprika);
//    }


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
