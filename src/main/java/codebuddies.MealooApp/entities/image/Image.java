package codebuddies.MealooApp.entities.image;

import codebuddies.MealooApp.entities.meal.Meal;

import javax.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String filePath;

    private String fileUrl;

    @ManyToOne
    private Meal meal;

    public Image() {
    }

    public Image(String filePath, String fileUrl, Meal meal) {
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.meal = meal;
    }

    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Meal getMeal() {
        return meal;
    }

    void setMeal(Meal meal) {
        this.meal = meal;
    }
}
