package codebuddies.MealooApp.entities.user;

public enum PhysicalActivity {
    NONE(10),
    LITTLE(12),
    MEDIUM(15),
    HIGH(17),
    HUGE(20);

    private final int multiplier;

    PhysicalActivity(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

}
