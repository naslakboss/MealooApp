package codebuddies.MealooApp.entities.product;

import javax.persistence.Embeddable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class Macronutrients {

    @NotNull
    @Min(value = 0, message = "Amount of Proteins could not be less than 0")
    @Max(value = 100, message = "Amount of Proteins in 100g of product could not be higher that 100g!")
    private int proteinsPer100g;

    @NotNull
    @Min(value = 0, message = "Amount of Carbohydrates could not be less than 0")
    @Max(value = 100, message = "Amount of Carbohydrates in 100g of product could not be higher that 100g!")
    private int carbohydratesPer100g;

    @NotNull
    @Min(value = 0, message = "Amount of Fats could not be less than 0")
    @Max(value = 100, message = "Amount of Fats in 100g of product could not be higher that 100g!")
    private int fatsPer100g;

    public Macronutrients() {
    }

    public Macronutrients(int proteinsPer100g, int carbohydratesPer100g, int fatPer100g) {
        this.proteinsPer100g = proteinsPer100g;
        this.carbohydratesPer100g = carbohydratesPer100g;
        this.fatsPer100g = fatPer100g;
    }

    public int getProteinsPer100g() {
        return proteinsPer100g;
    }

    public void setProteinsPer100g(int proteinPer100g) {
        this.proteinsPer100g = proteinPer100g;
    }

    public int getCarbohydratesPer100g() {
        return carbohydratesPer100g;
    }

    public void setCarbohydratesPer100g(int carbohydratesPer100g) {
        this.carbohydratesPer100g = carbohydratesPer100g;
    }

    public int getFatsPer100g() {
        return fatsPer100g;
    }

    public void setFatsPer100g(int fatPer100g) {
        this.fatsPer100g = fatPer100g;
    }

    @Override
    public String toString() {
        return "Macronutrients{" +
                "proteinsPer100g=" + proteinsPer100g +
                ", carbohydratesPer100g=" + carbohydratesPer100g +
                ", fatsPer100g=" + fatsPer100g +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Macronutrients that = (Macronutrients) o;
        return proteinsPer100g == that.proteinsPer100g &&
                carbohydratesPer100g == that.carbohydratesPer100g &&
                fatsPer100g == that.fatsPer100g;
    }

    @Override
    public int hashCode() {
        return Objects.hash(proteinsPer100g, carbohydratesPer100g, proteinsPer100g);
    }

}
