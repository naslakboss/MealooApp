package codebuddies.MealooApp.entities.product;



import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Macronutrients {

    private int proteinPer100g;

    private int carbohydratesPer100g;

    private int fatPer100g;

    public Macronutrients() {
    }

    public Macronutrients(int proteinPer100g, int carbohydratesPer100g, int fatPer100g) {
        this.proteinPer100g = proteinPer100g;
        this.carbohydratesPer100g = carbohydratesPer100g;
        this.fatPer100g = fatPer100g;
    }


    public int getProteinPer100g() {
        return proteinPer100g;
    }

    public void setProteinPer100g(int proteinPer100g) {
        this.proteinPer100g = proteinPer100g;
    }

    public int getCarbohydratesPer100g() {
        return carbohydratesPer100g;
    }

    public void setCarbohydratesPer100g(int carbohydratesPer100g) {
        this.carbohydratesPer100g = carbohydratesPer100g;
    }

    public int getFatPer100g() {
        return fatPer100g;
    }

    public void setFatPer100g(int fatPer100g) {
        this.fatPer100g = fatPer100g;
    }

    @Override
    public String toString() {
        return "Macronutrients{" +
                "proteinPer100g=" + proteinPer100g +
                ", carbohydratesPer100g=" + carbohydratesPer100g +
                ", fatPer100g=" + fatPer100g +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Macronutrients that = (Macronutrients) o;
        return proteinPer100g == that.proteinPer100g &&
                carbohydratesPer100g == that.carbohydratesPer100g &&
                fatPer100g == that.fatPer100g;
    }

    @Override
    public int hashCode() {
        return Objects.hash(proteinPer100g, carbohydratesPer100g, fatPer100g);
    }
}
