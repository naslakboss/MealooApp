package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodDiaryService {

    @Autowired
    FoodDiaryRepository foodDiaryRepository;

    public FoodDiary save(FoodDiary diary) {
        return foodDiaryRepository.save(diary);
    }
}
