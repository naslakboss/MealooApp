package codebuddies.MealooApp.entities.user;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FakeUserDetails {
    /**
     * This data will be used to calculate BMI for given USER
     */
    private Integer height;

    private Integer weight;

    private Integer age;

    private Sex sex;

    private PhysicalActivity physicalActivity;

    public int calculateBMI(Integer height, Integer weight){
        return weight / (height * height);
    }

    public int calculateCaloricDemand(Integer age, Sex sex, Integer height, Integer weight, PhysicalActivity physicalActivity){
        if(sex == Sex.MALE){
           int maleDemand = (66 + (14 * weight) + (5 * height) - (7 * age)) * (physicalActivity.getMultiplier()/10);
           return maleDemand;
        }
        int femaleDemand  = (655 + (10 * weight) + (2 * height) - (5 * age)) * (physicalActivity.getMultiplier()/10);
        return femaleDemand;

    }

    public FakeUserDetails() {
    }

    public FakeUserDetails(Integer height, Integer weight, Integer age, Sex sex, PhysicalActivity physicalActivity) {
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.sex = sex;
        this.physicalActivity = physicalActivity;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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
        return "FakeUserDetails{" +
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
        FakeUserDetails that = (FakeUserDetails) o;
        return height == that.height &&
                weight == that.weight &&
                age == that.age &&
                sex == that.sex &&
                physicalActivity == that.physicalActivity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, weight, age, sex, physicalActivity);
    }
}
