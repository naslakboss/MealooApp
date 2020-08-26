package codebuddies.MealooApp.entities.meal;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MealMacronutrients {

    private int totalProteins;

    private int totalCarbohydrates;

    private int totalFats;

    public MealMacronutrients() {
    }

    public MealMacronutrients(int totalProteins, int totalCarbohydrates, int totalFats) {
        this.totalProteins = totalProteins;
        this.totalCarbohydrates = totalCarbohydrates;
        this.totalFats = totalFats;
    }

    public int getTotalProteins() {
        return totalProteins;
    }

    public void setTotalProteins(int totalProteins) {
        this.totalProteins = totalProteins;
    }

    public int getTotalCarbohydrates() {
        return totalCarbohydrates;
    }

    public void setTotalCarbohydrates(int totalCarbohydrates) {
        this.totalCarbohydrates = totalCarbohydrates;
    }

    public int getTotalFats() {
        return totalFats;
    }

    public void setTotalFats(int totalFats) {
        this.totalFats = totalFats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealMacronutrients that = (MealMacronutrients) o;
        return totalProteins == that.totalProteins &&
                totalCarbohydrates == that.totalCarbohydrates &&
                totalFats == that.totalFats;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalProteins, totalCarbohydrates, totalFats);
    }

    @Override
    public String toString() {
        return "MealMacronutrients{" +
                "totalProteins=" + totalProteins +
                ", totalCarbohydrates=" + totalCarbohydrates +
                ", totalFats=" + totalFats +
                '}';
    }
}
