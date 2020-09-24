package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.dto.MealooUserDTO;
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

    Optional<List<FoodDiary>> findByDate(LocalDate date);

    Page<FoodDiary> findByMealooUser(MealooUser mealooUser, Pageable pageable);


    FoodDiary findByMealooUserAndDate(MealooUser mealooUser, LocalDate date);

    List<FoodDiary> findByMealooUserAndDateAfter(MealooUser mealooUser, LocalDate after);
}
