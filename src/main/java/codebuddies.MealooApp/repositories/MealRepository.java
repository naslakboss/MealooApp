package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.meal.Meal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface MealRepository extends JpaRepository<Meal, String> {

    Meal save(Meal meal);

    boolean existsByName(String name);

    Meal findByName(String name);

    void deleteByName(String name);

    List<Meal> findByTotalCaloriesBetween(int lowerBorder, int upperBorder);
}
