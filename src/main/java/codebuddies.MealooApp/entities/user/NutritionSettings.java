package codebuddies.MealooApp.entities.user;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;

@Embeddable
public class NutritionSettings {

    private int dailyCaloricGoal;

    public int getDailyCaloricGoal() {
        return dailyCaloricGoal;
    }

    public void setDailyCaloricGoal(int dailyCaloricGoal) {
        this.dailyCaloricGoal = dailyCaloricGoal;
    }

    public NutritionSettings() {
    }

    public NutritionSettings(int dailyCaloricGoal) {
        this.dailyCaloricGoal = dailyCaloricGoal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NutritionSettings that = (NutritionSettings) o;
        return dailyCaloricGoal == that.dailyCaloricGoal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyCaloricGoal);
    }

    @Override
    public String toString() {
        return "NutritionSettings{" +
                "dailyCaloricGoal=" + dailyCaloricGoal +
                '}';
    }
}
