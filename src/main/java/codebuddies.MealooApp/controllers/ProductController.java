package codebuddies.MealooApp.controllers;

import codebuddies.MealooApp.dataProviders.ProductDTO;
import codebuddies.MealooApp.dataProviders.ProductFacade;
import codebuddies.MealooApp.entities.product.Macronutrients;
import codebuddies.MealooApp.entities.product.Product;
import codebuddies.MealooApp.entities.product.ProductType;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.services.ProductService;
//import mealoapp.MealooAppp.services.ProductTypeService;
//import codebuddies.MealooApp.services.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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
    public ResponseEntity<Product> addProduct(@RequestBody @Valid Product product) {
        if(productService.existsByName(product.getName())) throw new EntityAlreadyFoundException("codebuddies.MealooApp/entities/product");
        productService.save(product);
        return ResponseEntity.created(URI.create("/" + product.getName())).body(product);
    }

    @PatchMapping("/patch/{name}")
    public ResponseEntity<Product> patchProductByName(@PathVariable String name, @Valid Product product) {
        Product patchedProduct = productService.findByName(name);
        return patchedProduct != null ? ResponseEntity.ok(productService.updateByName(name, product)) : ResponseEntity.notFound().build();

    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity deleteByName(@PathVariable String name) {
        if (!productService.existsByName(name)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("codebuddies.MealooApp/entities/product " + name + " was successfully deleted from Repository");
    }
}

