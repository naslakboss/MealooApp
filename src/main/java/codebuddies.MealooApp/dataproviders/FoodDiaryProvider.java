package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.FoodDiaryDTO;
import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.services.MealooUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodDiaryProvider {

    private FoodDiaryService foodDiaryService;

    private MealooUserService mealooUserService;

    private ModelMapper modelMapper;

    @Autowired

    public FoodDiaryProvider(FoodDiaryService foodDiaryService, MealooUserService mealooUserService, ModelMapper modelMapper) {
        this.foodDiaryService = foodDiaryService;
        this.mealooUserService = mealooUserService;
        this.modelMapper = modelMapper;
    }

    public FoodDiaryDTO findTodaysDiary(String name) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(name);
        return modelMapper.map(foodDiaryService.findTodaysDiary(user), FoodDiaryDTO.class);
    }

    public FoodDiaryDTO findDiaryOfDay(String name, String date) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(name);
        return modelMapper.map(foodDiaryService.findDiaryOfDay(user, date), FoodDiaryDTO.class);
    }

    public FoodDiaryDTO createNewDiary(String username) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(username);
        return modelMapper.map(foodDiaryService.createNewDiary(user), FoodDiaryDTO.class);
    }

    public List<FoodDiaryDTO> findAllDiaries(String username, Pageable pageable) throws ResourceNotFoundException {
        MealooUser user = mealooUserService.findByUsername(username);
        return foodDiaryService.findAllDiariesPageable(user, pageable).stream()
                .map(foodDiary -> modelMapper.map(foodDiary, FoodDiaryDTO.class))
                    .collect(Collectors.toList());
    }
}
