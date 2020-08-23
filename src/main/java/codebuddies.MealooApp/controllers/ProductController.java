package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.ProductDTO;
import codebuddies.MealooApp.dataProviders.ProductFacade;
import codebuddies.MealooApp.entities.product.Product;
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
import javax.xml.bind.ValidationException;
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
    public ResponseEntity<ProductDTO> findProductByName(@PathVariable String name) {
        ProductDTO searchedProduct = productFacade.getProductByName(name);
        return searchedProduct != null ? ResponseEntity.ok(searchedProduct) : ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product)  {
        productService.save(product);
        return ResponseEntity.ok(productFacade.getProductByName(product.getName()));
    }

    @PostMapping("/testadd")
    public ResponseEntity<String> testthisshit(@Valid @RequestBody Product product){
        return ResponseEntity.ok("Product is valid");
    }

    @PatchMapping("/patch/{name}")
    public ResponseEntity<ProductDTO> patchProductByName(@PathVariable String name, @Valid Product product) {
        Product oldProduct = productService.findByName(name);
        if(oldProduct == null){
            return ResponseEntity.notFound().build();
        }
        productService.updateByName(name, oldProduct);
        return ResponseEntity.ok(productFacade.getProductByName(name));


    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity deleteByName(@PathVariable String name) {
        if (!productService.existsByName(name)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Product " + name + " was successfully deleted from Repository");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }
}
