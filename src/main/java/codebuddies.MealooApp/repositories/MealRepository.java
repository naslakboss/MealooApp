package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MealRepository extends JpaRepository<Meal, String> {

    boolean existsByName(String name);

    Meal findByName(String name);

    void deleteByName(String name);
}
