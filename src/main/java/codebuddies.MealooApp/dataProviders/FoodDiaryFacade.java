package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.entities.user.FakeUser;
import codebuddies.MealooApp.services.FakeUserService;
import codebuddies.MealooApp.services.FoodDiaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
