package codebuddies.MealooApp.entities.user;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MealooUserDetails {
    /**
     * This data will be used to calculate BMI for given USER
     */
    private Integer height;

    private Integer weight;

    private Integer age;

    private Sex sex;

    private PhysicalActivity physicalActivity;

    public MealooUserDetails() {
    }

    public MealooUserDetails(Integer height, Integer weight, Integer age, Sex sex, PhysicalActivity physicalActivity) {
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.sex = sex;
        this.physicalActivity = physicalActivity;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }

    public void setPhysicalActivity(PhysicalActivity physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    @Override
    public String toString() {
        return "MealooUserDetails{" +
                "height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", sex=" + sex +
                ", physicalActivity=" + physicalActivity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealooUserDetails that = (MealooUserDetails) o;
        return Objects.equals(height, that.height) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(age, that.age) &&
                sex == that.sex &&
                physicalActivity == that.physicalActivity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, weight, age, sex, physicalActivity);
    }
}
