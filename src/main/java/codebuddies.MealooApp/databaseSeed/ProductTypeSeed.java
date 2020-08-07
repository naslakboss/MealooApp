package codebuddies.MealooApp.databaseSeed;

import codebuddies.MealooApp.entities.ProductType;
import codebuddies.MealooApp.repositories.ProductTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductTypeSeed {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeSeed(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    public List<ProductType> initProductTypes(){
        ProductType meat = new ProductType("MEAT"
                , "Meat contains a lot of proteins and some saturated fats");
        ProductType dairy = new ProductType("DAIRY"
                , "Including domesticated milk and dairy animal products and eggs");
        ProductType grains = new ProductType("GRAINS"
                , "They are the main source of carbohydrates in the diet." +
                " They are processed into flours, groats, cereals, bran, pasta, cereal coffees and plant milks");
        ProductType fruits = new ProductType("FRUITS"
                , "Edible parts of trees or shrubs");
        ProductType vegatables = new ProductType("VEGATABLES"
                , "Very healty, contains lot of vitamins and minearls");
        List<ProductType> listOfProductTypes = Arrays.asList(meat, dairy, grains, fruits, vegatables);
        return listOfProductTypes;
    }
}
