package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.user.FoodDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
    Optional<List<FoodDiary>> findByDate(LocalDate date);
}
