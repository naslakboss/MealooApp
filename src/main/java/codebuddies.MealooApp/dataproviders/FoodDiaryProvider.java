package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.FoodDiaryDTO;
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

    public Page<FoodDiaryDTO> getAllDiaries(MealooUser user, Pageable pageable){
        //todo what if empty?
        return diaryRepository.findByMealooUser(user, pageable)
                .map(diary -> modelMapper.map(diary, FoodDiaryDTO.class));
    }

    public FoodDiaryDTO getDiaryByDate(MealooUser user, LocalDate date){
         FoodDiary diary = diaryRepository.findByMealooUserAndDate(user, date);
         return modelMapper.map(diary, FoodDiaryDTO.class);
    }

    public FoodDiaryDTO createDiary(MealooUser user, LocalDate currentDate) {
        FoodDiary diary = new FoodDiary(new ArrayList<>(), currentDate, user);
        diaryRepository.save(diary);
        return modelMapper.map(diary, FoodDiaryDTO.class);
    }

    public FoodDiaryDTO updateDiary(FoodDiaryDTO diaryDTO, MealooUser user){
        FoodDiary diary = modelMapper.map(diaryDTO, FoodDiary.class);
        diary.setMealooUser(user);
        diaryRepository.save(diary);
        return diaryDTO;
    }

    public List<FoodDiaryDTO> rejectMealsFromThreeDaysBack(MealooUser user, LocalDate threeDaysBack) {
        List<FoodDiary> list = diaryRepository.findByMealooUserAndDateAfter(user, threeDaysBack);
        return list.stream().map(diary -> modelMapper.map(diary, FoodDiaryDTO.class))
                .collect(Collectors.toList());
    }
}
