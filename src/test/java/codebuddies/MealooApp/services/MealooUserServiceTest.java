package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

//@MockitoSettings(strictness = Strictness.STRICT_STUBS)
//@ExtendWith(SpringExtension.class)
//class MealooUserServiceTest {
//
//    @Mock
//    MealooUserRepository userRepository;
//
//    MealooUserService userService;
//
//    MealooUser user1;
//    MealooUser user2;
//    FoodDiary diary;
//    List<MealooUser> listOfUsers;
//
//    @BeforeEach
//    void setUp(){
//        user1 = new MealooUser("Tester", "TopSecret", "imtester@gmail.com");
//        user1.setMealooUserDetails(new MealooUserDetails(177, 83, 25, Sex.MALE, PhysicalActivity.MEDIUM));
//        user2 = new MealooUser("Client", "pass", "cleint@gmail.com");
//        diary = new FoodDiary(Collections.emptyList(), LocalDate.now(), user1);
//        listOfUsers = List.of(user1, user2);
//        userService = new MealooUserService(userRepository);
//    }
//
//    Page<MealooUser> createTestPage(Pageable pageable){
//        return new PageImpl<>(listOfUsers, pageable, listOfUsers.size());
//    }
//    @Test
//    void shouldReturnListOfUsers() {
//        //given
//        Pageable pageable = PageRequest.of(0, 2);
//        given(userRepository.findAll(pageable)).willReturn(createTestPage(pageable));
//        //when
//        List<MealooUser> users = userService.findAll(pageable).getContent();
//        //then
//        assertAll(
//                () -> assertThat(users.size(), equalTo(2)),
//                () -> assertThat(users.get(0), equalTo(user1)),
//                () -> assertThat(users.get(1), equalTo(user2))
//        );
//    }
//
//    @Test
//    void shouldSaveUser() {
//        //given
//        given(userRepository.save(user1)).willReturn(user1);
//        //when
//        MealooUser result = userService.save(user1);
//        //then
//        assertThat(result, equalTo(user1));
//    }
//
//
//    @Test
//    void shouldFindUserWhenItDoesExist() {
//        //given
//        given(userRepository.findByUsername("client")).willReturn(user2);
//        //when
//        MealooUser user = userService.findByUsername("client");
//        //then
//        assertThat(user, equalTo(user2));
//    }
//
//    @Test
//    void shouldThrowAResourceNotFoundExceptionWhenUsernameIsNotCorrect(){
//        //given + when
//        given(userRepository.findByUsername("boss")).willThrow(ResourceNotFoundException.class);
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> userService.findByUsername("boss"));
//    }
//
//
//    @Test
//    void shouldUpdateUserDataIfExist() {
//        //given
//        given(userRepository.findByUsername("Tester")).willReturn(user1);
//        MealooUser user3 = new MealooUser("Manager", "bCryptEncoded", "managermail@gmail.com");
//        user3.setMealooUserDetails(new MealooUserDetails(175, 80, 32, Sex.MALE, PhysicalActivity.LITTLE));
//        //when
//        MealooUser result = userService.updateByUsername("Tester", user3);
//        //then
//        assertAll(
//                () -> assertThat(result.getMealooUserDetails().getAge(), equalTo(32)),
//                () -> assertThat(result.getMealooUserDetails().getHeight(), equalTo(175)),
//                () -> assertThat(result.getMealooUserDetails().getPhysicalActivity(), equalTo(PhysicalActivity.LITTLE))
//        );
//    }
//
//    @Test
//    void shouldDeleteUserIfExists(){
//        //given
//        given(userRepository.findByUsername("Tester")).willReturn(user1);
//        //when
//        userService.deleteByUsername("Tester");
//        //then
//        verify(userRepository, times(1)).deleteByUsername("Tester");
//    }
//    @Test
//    void shouldThrowaResourceNotFoundExceptionWhenUserDoesNotExist(){
//        //given + when
//        given(userRepository.findByUsername("Yeti")).willReturn(null);
//        //then
//        assertThrows(ResourceNotFoundException.class, () ->
//                userService.deleteByUsername("Yeti"));
//    }
//
//    @Test
//    void shouldThrowAResourceNotFoundExceptionWhenUpdateNotExistedUser(){
//        //given + when
//        given(userRepository.findByUsername("Clerk")).willThrow(ResourceNotFoundException.class);
//        //then
//        assertThrows(ResourceNotFoundException.class, () -> userService.updateByUsername("Clerk", user2));
//    }
//
//    @Test
//    void shouldCalculateBMIAndCaloricDemandAndAlsoReturnSomeInformation() {
//        //given
//        Double bmi = user1.getMealooUserDetails().calculateBMI();
//        int caloricDemand = user1.getMealooUserDetails().calculateCaloricDemand();
//        //when
//        Map result = userService.calculateBMIAndCaloricDemand(user1);
//        //then
//        assertAll(
//                () -> assertThat(result.get("Your BMI is : "), equalTo(bmi)),
//                () -> assertThat(result.get("Your caloric demand is :"), equalTo((double)caloricDemand)),
//                () -> assertThat(result.get("If you want to gain about 0.5kg per week, you should eat about"), equalTo((double)caloricDemand + 500))
//        );
//    }
//}