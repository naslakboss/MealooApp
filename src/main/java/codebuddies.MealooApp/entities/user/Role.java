package codebuddies.MealooApp.entities.user;

import javax.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealooUserRole name;

    public Role() {
    }

    public Role(MealooUserRole name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MealooUserRole getName() {
        return name;
    }

    public void setName(MealooUserRole name) {
        this.name = name;
    }
}
