package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.meal.Meal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MealRepository extends JpaRepository<Meal, String> {

    boolean existsByName(String name);

    Optional<Meal> findByName(String name);

    void deleteByName(String name);

    List<Meal> findByTotalCaloriesBetween(int lowerBorder, int upperBorder);
}
