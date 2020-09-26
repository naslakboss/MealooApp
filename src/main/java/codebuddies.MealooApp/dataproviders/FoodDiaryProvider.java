package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.user.FoodDiary;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
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

    public Page<FoodDiaryDTO> getAllDiaries(MealooUserDTO userDTO, Pageable pageable) {
        MealooUser user = modelMapper.map(userDTO, MealooUser.class);

        return diaryRepository.findByMealooUser(user, pageable)
                .map(diary -> modelMapper.map(diary, FoodDiaryDTO.class));
    }

    public FoodDiaryDTO getDiaryByDate(MealooUserDTO userDTO, LocalDate date) {
        MealooUser user = modelMapper.map(userDTO, MealooUser.class);
        FoodDiary diary = diaryRepository.findByMealooUserAndDate(user, date).orElseThrow(() ->
                new RuntimeException("Diary of " + date + " does not exist in databse"));

        return modelMapper.map(diary, FoodDiaryDTO.class);
    }

    public FoodDiaryDTO createDiary(MealooUserDTO userDTO, LocalDate currentDate) {
        MealooUser user = modelMapper.map(userDTO, MealooUser.class);
        FoodDiary diary = new FoodDiary(new ArrayList<>(), currentDate, user);
        diaryRepository.save(diary);

        return modelMapper.map(diary, FoodDiaryDTO.class);
    }

    public FoodDiaryDTO updateDiary(FoodDiaryDTO diaryDTO, MealooUserDTO userDTO) {
        MealooUser user = modelMapper.map(userDTO, MealooUser.class);
        FoodDiary diary = modelMapper.map(diaryDTO, FoodDiary.class);
        diary.setMealooUser(user);
        diaryRepository.save(diary);

        return diaryDTO;
    }

    public List<FoodDiaryDTO> rejectMealsFromThreeDaysBack(MealooUserDTO userDTO, LocalDate threeDaysBack) {
        MealooUser user = modelMapper.map(userDTO, MealooUser.class);
        List<FoodDiary> list = diaryRepository.findByMealooUserAndDateAfter(user, threeDaysBack);

        return list.stream()
                .map(diary -> modelMapper.map(diary, FoodDiaryDTO.class))
                .collect(Collectors.toList());
    }
}
