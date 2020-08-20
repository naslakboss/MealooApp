package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.services.MealooUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodDiaryFacade {

    private FoodDiaryService foodDiaryService;

    private MealooUserService mealooUserService;

    private ModelMapper modelMapper;

    @Autowired

    public FoodDiaryFacade(FoodDiaryService foodDiaryService, MealooUserService mealooUserService, ModelMapper modelMapper) {
        this.foodDiaryService = foodDiaryService;
        this.mealooUserService = mealooUserService;
        this.modelMapper = modelMapper;
    }

    public FoodDiaryDTO findTodayDiary(String name){
        MealooUser user = mealooUserService.findByUsername(name);
        return modelMapper.map(foodDiaryService.findTodayDiary(user), FoodDiaryDTO.class);
    }

    public FoodDiaryDTO findDiaryOfGivenDay(String name, String date){
        MealooUser user = mealooUserService.findByUsername(name);
        return modelMapper.map(foodDiaryService.findDiaryOfGivenDate(user, date), FoodDiaryDTO.class);
    }

    public FoodDiaryDTO createNewFoodDiary(String username){
        MealooUser user = mealooUserService.findByUsername(username);
        return modelMapper.map(foodDiaryService.createNewFoodDiary(user), FoodDiaryDTO.class);
    }

    public List<FoodDiaryDTO> getAllDiariesForGivenUser(String username){
        MealooUser user = mealooUserService.findByUsername(username);
        return foodDiaryService.findAllDiariesForUser(user).stream()
                .map(foodDiary -> modelMapper.map(foodDiary, FoodDiaryDTO.class))
                    .collect(Collectors.toList());
    }
}
