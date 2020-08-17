package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.services.FakeUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodDiaryFacade {

    private FoodDiaryService foodDiaryService;

    private FakeUserService fakeUserService;

    private ModelMapper modelMapper;

    @Autowired

    public FoodDiaryFacade(FoodDiaryService foodDiaryService, FakeUserService fakeUserService, ModelMapper modelMapper) {
        this.foodDiaryService = foodDiaryService;
        this.fakeUserService = fakeUserService;
        this.modelMapper = modelMapper;
    }

    public FoodDiaryDTO findTodayDiary(String name){
        FakeUser user = fakeUserService.findByUsername(name);
        return modelMapper.map(foodDiaryService.findTodayDiary(user), FoodDiaryDTO.class);
    }

    public FoodDiaryDTO findDiaryOfGivenDay(String name, String date){
        FakeUser user = fakeUserService.findByUsername(name);
        return modelMapper.map(foodDiaryService.findDiaryOfGivenDate(user, date), FoodDiaryDTO.class);
    }

    public FoodDiaryDTO createNewFoodDiary(String username){
        FakeUser user = fakeUserService.findByUsername(username);
        return modelMapper.map(foodDiaryService.createNewFoodDiary(user), FoodDiaryDTO.class);
    }

    public List<FoodDiaryDTO> getAllDiariesForGivenUser(String username){
        FakeUser user = fakeUserService.findByUsername(username);
        return foodDiaryService.findAllDiariesForUser(user).stream()
                .map(foodDiary -> modelMapper.map(foodDiary, FoodDiaryDTO.class))
                    .collect(Collectors.toList());
    }
}
