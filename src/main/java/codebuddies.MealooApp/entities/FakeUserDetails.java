package codebuddies.MealooApp.entities;

import javax.persistence.Embeddable;

@Embeddable
public class FakeUserDetails {

    private int height;

    private int weight;

    private int age;

    private Sex sex;

    private String activity;
    // This could be enum or simple algorithm which is used to calculate BMI -> They are exist on the Internet

    // This data will calculate how much user need to eat for keeping weight stable.
}
