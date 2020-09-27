package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.meal.MealMacronutrients;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.repositories.FoodDiaryRepository;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodDiaryProvider {

    private ModelMapper modelMapper = new ModelMapper();

    private FoodDiaryRepository diaryRepository;

    public FoodDiaryProvider(FoodDiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public Page<FoodDiaryDTO> getAllDiaries(int id, Pageable pageable) {

        return diaryRepository.findByMealooUserId(id, pageable)
                .map(diary -> modelMapper.map(diary, FoodDiaryDTO.class));
    }

    public FoodDiaryDTO getDiaryByDate(int id, LocalDate date) {
        FoodDiary diary = diaryRepository.findByMealooUserIdAndDate(id, date).orElseThrow(() ->
                new RuntimeException("Diary of date : " + date + " for user with ID : " + id +" does not exist"));

        return modelMapper.map(diary, FoodDiaryDTO.class);
    }

    public FoodDiaryDTO createDiary( LocalDate currentDate, MealooUserDTO user) {
        FoodDiaryDTO foodDiaryDTO =
                new FoodDiaryDTO(currentDate, new ArrayList<MealDTO>(),
                        new MealMacronutrients(0,0,0), 0, 0.0, user);
        FoodDiary diary = modelMapper.map(foodDiaryDTO, FoodDiary.class);
        diaryRepository.save(diary);
        return modelMapper.map(diary, FoodDiaryDTO.class);
    }

    public FoodDiaryDTO updateDiary(FoodDiaryDTO diaryDTO) {
        FoodDiary diary = modelMapper.map(diaryDTO, FoodDiary.class);
        diary.setId(diaryDTO.getId());
        diaryRepository.save(diary);
        return diaryDTO;
    }

    public List<FoodDiaryDTO> rejectMealsFromThreeDaysBack(long id, LocalDate threeDaysBack) {
        List<FoodDiary> list = diaryRepository.findByMealooUserIdAndDateAfter(id, threeDaysBack);

        return list.stream()
                .map(diary -> modelMapper.map(diary, FoodDiaryDTO.class))
                .collect(Collectors.toList());
    }


}
