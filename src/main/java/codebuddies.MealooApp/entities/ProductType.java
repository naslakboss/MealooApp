package codebuddies.MealooApp.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class ProductType {

    @Id
    private String type;

    private String description;

    public ProductType() {
    }

    public ProductType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public ProductType(String type) {
        this.type = type;
    }

    public String getProductType() {
        return type;
    }

    public void setProductType(String productType) {
        this.type = productType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
