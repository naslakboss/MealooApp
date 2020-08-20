package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.user.FoodDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
    FoodDiary findByDate(LocalDate date);
}
