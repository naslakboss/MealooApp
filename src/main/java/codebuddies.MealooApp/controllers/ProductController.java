package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.ProductDTO;
import codebuddies.MealooApp.dataProviders.ProductFacade;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.exceptions.ValidationException;
import codebuddies.MealooApp.services.ProductService;
//import mealoapp.MealooAppp.services.ProductTypeService;
//import codebuddies.MealooApp.services.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import javax.validation.executable.ValidateOnExecution;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    ProductService productService;

    ProductFacade productFacade;

    @Autowired
    public ProductController(ProductService productService, ProductFacade productFacade) {
        this.productService = productService;
        this.productFacade = productFacade;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB() {
//
//        Product egg = new Product("Eggs", 10, 155,
//                new Macronutrients(13, 1, 10), ProductType.DAIRY);
//        Product bread = new Product("Bread", 3, 264,
//                new Macronutrients(9, 50, 3), ProductType.GRAINS);
//        Product milk = new Product("Milk", 4, 100,
//                new Macronutrients(3, 4, 4), ProductType.DAIRY);
//        Product beef = new Product("Beef", 30, 300,
//                new Macronutrients(30, 5, 30), ProductType.MEAT);
//        Product chicken = new Product("Chicken", 15, 100,
//                new Macronutrients(22, 0, 1), ProductType.MEAT);
//        Product pasta = new Product("Pasta", 8, 343,
//                new Macronutrients(12, 65, 2), ProductType.MEAT);
//        Product strawberry = new Product("Strawberry", 5, 33,
//                new Macronutrients(2, 12, 2), ProductType.DAIRY);
//        productService.save(egg);
//        productService.save(bread);
//        productService.save(milk);
//        productService.save(beef);
//        productService.save(chicken);
//        productService.save(pasta);
//        productService.save(strawberry);
//
//        Product piers = new Product("ChickenBreast", 2, 170,
//                new Macronutrients(31, 0, 4), ProductType.MEAT);
//        Product rice = new Product("WhiteRice", 1, 360,
//                new Macronutrients(7, 80, 1), ProductType.GRAINS);
//        Product paprika = new Product("Paprika", 3, 28,
//                new Macronutrients(1, 7, 1), ProductType.VEGETABLES);
//        productService.save(piers);
//        productService.save(rice);
//        productService.save(paprika);
//    }


    @GetMapping("")
    public ResponseEntity<List<ProductDTO>> findAllProducts() {
        return ResponseEntity.ok(productFacade.getAllProducts());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductDTO> findProductByName(@PathVariable String name) throws ResourceNotFoundException {
        ProductDTO searchedProduct = productFacade.getProductByName(name);
        return ResponseEntity.ok().body(searchedProduct);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product) throws ResourceNotFoundException, ValidationException {
        productService.save(product);
        return ResponseEntity.ok(productFacade.getProductByName(product.getName()));
    }

    @PatchMapping("/patch/{name}")
    public ResponseEntity<ProductDTO> patchProductByName(@PathVariable String name, @Valid Product product) throws ResourceNotFoundException {
        productService.updateByName(name, product);
        return ResponseEntity.ok(productFacade.getProductByName(name));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity deleteByName(@PathVariable String name) throws ResourceNotFoundException {
        productService.deleteByName(name);
        return ResponseEntity.ok("Product " + name + " was successfully deleted from Repository");
    }
}
