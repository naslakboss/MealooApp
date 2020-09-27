package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.entities.user.MealooUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {

    Page<FoodDiary> findByMealooUserId(long mealooUserId, Pageable pageable);

    List<FoodDiary> findByMealooUserIdAndDateAfter(long mealooUserId, LocalDate after);

    Optional<FoodDiary> findByMealooUserIdAndDate(long mealooUserId, LocalDate date);
}
