package codebuddies.MealooApp.entities.meal;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class MealMacronutrients {

    @NotNull
    @Min(value = 0, message = "Amount of Proteins could not be less than 0")
    @Max(value = 100, message = "Amount of Proteins in 100g of product could not be higher that 100g!")
    private int totalProteins;

    @NotNull
    @Min(value = 0, message = "Amount of Carbohydrates could not be less than 0")
    @Max(value = 100, message = "Amount of Carbohydrates in 100g of product could not be higher that 100g!")
    private int totalCarbohydrates;

    @NotNull
    @Min(value = 0, message = "Amount of Fats could not be less than 0")
    @Max(value = 100, message = "Amount of Fats in 100g of product could not be higher that 100g!")
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
