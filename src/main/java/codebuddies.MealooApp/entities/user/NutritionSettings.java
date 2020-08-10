package codebuddies.MealooApp.entities.user;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class NutritionSettings {

    private int dailyCaloricGoal;

    public int getDailyCaloricGoal() {
        return dailyCaloricGoal;
    }

    public void setDailyCaloricGoal(int dailyCaloricGoal) {
        this.dailyCaloricGoal = dailyCaloricGoal;
    }
}
