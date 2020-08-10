package codebuddies.MealooApp.controllers;

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

    @Autowired
    ProductService productService;

//     Fill database
//    @EventListener(ApplicationReadyEvent.class)
//    public void fillDB() {
//
//        Product egg = new Product("Eggs", 1, 140,
//                new Macronutrients(13, 1, 10), ProductType.DAIRY);
//        Product bread = new Product("Bread", 3, 264,
//                new Macronutrients(9, 50, 3), ProductType.GRAINS);
//        Product milk = new Product("Milk", 4, 100,
//                new Macronutrients(3, 4, 4), ProductType.DAIRY);
//        Product beef = new Product("Beef", 30, 300,
//                new Macronutrients(30, 5, 30), ProductType.MEAT);
//        productService.save(egg);
//        productService.save(bread);
//        productService.save(milk);
//        productService.save(beef);
//    }


    @GetMapping("")
    public ResponseEntity<List<Product>> findAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Product> findProductByName(@PathVariable String name) {
        Product searchedProduct = productService.findByName(name);
        return searchedProduct != null ? ResponseEntity.ok(searchedProduct) : ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid Product product) {
        if(productService.existsByName(product.getName())) throw new EntityAlreadyFoundException("codebuddies.MealooApp/entities/product");
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

