package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.MealooUserMapper;
import codebuddies.MealooApp.dto.MealooUserDTO;
import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class MealooUserServiceTest {

    @Mock
    MealooUserMapper userProvider;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    MealooUserDTO user1;
    MealooUserDTO user2;

    List<MealooUserDTO> listOfUsers;

    MealooUserService userService;


    @BeforeEach
    void setUp() {
        user1 = new MealooUserDTO(1L, "Admin", "pass", Collections.singletonList(new Role(MealooUserRole.ROLE_ADMIN)), "admin@gmail.com"
                , new NutritionSettings(3500)
                , new MealooUserDetails(180, 90, 22, Sex.MALE, PhysicalActivity.HIGH), Collections.emptyList());

        user2 = new MealooUserDTO(2L, "User", "secret", Collections.singletonList(new Role(MealooUserRole.ROLE_ADMIN)), "user@gmail.com"
                , new NutritionSettings(2500)
                , new MealooUserDetails(170, 80, 27, Sex.FEMALE, PhysicalActivity.LITTLE), Collections.emptyList());

        listOfUsers = List.of(user1, user2);

        userService = new MealooUserService(userProvider, passwordEncoder);
    }

    Page<MealooUserDTO> createTestPage(Pageable pageable) {
        List<MealooUserDTO> list = listOfUsers;
        return new PageImpl<>(list, pageable, list.size());
    }

    @Test
    @DisplayName("Check if all users are returned")
    void shouldReturnListOfUsers() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        given(userProvider.getAllUsers(pageable)).willReturn(createTestPage(pageable));

        //when
        Page<MealooUserDTO> users = userService.getAllUsers(pageable);

        //then
        assertAll(
                () -> assertThat(users.getSize(), equalTo(2)),
                () -> assertThat(users.getContent().get(0), equalTo(user1)),
                () -> assertThat(users.getContent().get(1), equalTo(user2))
        );
    }

    @Test
    @DisplayName("Check if user is returned if exists")
    void shouldFindUserWhenItDoesExist() {
        //given
        given(userProvider.getUserByUsername("client")).willReturn(user2);

        //when
        MealooUserDTO user = userService.getUserByUsername("client");

        //then
        assertThat(user, equalTo(user2));
    }

    @Test
    @DisplayName("Check if all user is created")
    void shouldCreateUserWhenNameIsNotUsed() {
        //given
        given(userProvider.existsByUsername(user1.getUsername())).willReturn(false);
        given(userProvider.createUser(user1)).willReturn(user1);
        user1.setPassword("password");

        //when
        MealooUserDTO result = userService.createUser(user1);

        //then
        assertThat(result, equalTo(user1));
    }

    @Test
    @DisplayName("Check if exception is thrown when trying to create user with existed in database name")
    void shouldThrowEntityNotFoundExceptionWhenUserAlreadyExist() {
        //given + when
        given(userProvider.existsByUsername(user2.getUsername())).willReturn(true);

        //then
        assertThrows(EntityAlreadyFoundException.class, () ->
                userService.createUser(user2));
    }

    @Test
    @DisplayName("Check if user is updated")
    void shouldUpdateUserDataIfExist() {
        //given
        given(userProvider.existsByUsername("Admin")).willReturn(true);
        given(userProvider.getUserByUsername("Admin")).willReturn(user1);
        MealooUserDTO user3 = new MealooUserDTO(3L, "Manager", "secretPIN", Collections.singletonList(new Role(MealooUserRole.ROLE_MODERATOR)), "manager@gmail.com"
                , new NutritionSettings(3000)
                , new MealooUserDetails(178, 85, 30, Sex.FEMALE, PhysicalActivity.NONE), Collections.emptyList());
        given(userProvider.updateUser(user3)).willReturn(user3);

        //when
        MealooUserDTO result = userService.updateUserByUsername(user3, "Admin");

        //then
        assertAll(
                () -> assertThat(result.getMealooUserDetails().getAge(), equalTo(30)),
                () -> assertThat(result.getMealooUserDetails().getHeight(), equalTo(178)),
                () -> assertThat(result.getMealooUserDetails().getPhysicalActivity(), equalTo(PhysicalActivity.NONE))
        );
    }

    @Test
    @DisplayName("Check if exception is thrown when trying update nonexistent user")
    void shouldThrowResourceNotFoundExceptionIfUserUserDoesNotExist() {
        //given + then
        given(userProvider.existsByUsername("Jasiek")).willReturn(false);

        //then
        assertThrows(ResourceNotFoundException.class, () ->
                userService.updateUserByUsername(any(), "Jasiek"));
    }

    @Test
    @DisplayName("Check if all user is deleted when had existed before")
    void shouldDeleteUserIfExists() {
        //given + when
        userService.deleteByUsername("User");

        //then
        verify(userProvider, times(1)).deleteUserByUsername("User");
    }

    @Test
    @DisplayName("Check if all bmi index is calculated")
    void shouldCalculateProperBMI(){
        //given + when
        MealooUserDetails userDetails = user1.getMealooUserDetails();
        double expectedResult = (double) userDetails.getWeight() * 10000 / Math.pow(userDetails.getHeight(),2);
        double bmi = userService.calculateBMI(user1);

        //then
        assertThat(bmi, equalTo(expectedResult));
    }

    @Test
    @DisplayName("Check if caloric demand for male is calculated")
    void shouldCalculateProperCaloricDemandForManIfSexEqualsToMale(){
        //given + when
        MealooUserDetails details = user1.getMealooUserDetails();
        int expectedResult = (66 + (14 * details.getWeight()) + (5 * details.getHeight())
                - (6 * details.getAge())) * (details.getPhysicalActivity().getMultiplier()/10);
        int caloricDemand = userService.calculateCaloricDemand(user1);

        //then
        assertThat(caloricDemand, equalTo(expectedResult));
    }


    @Test
    @DisplayName("Check if caloric demand for female is calculated")
    void shouldReturnBMIAndCaloricDemandAndAlsoReturnSomeInformation() {
        //given
        given(userProvider.getUserByUsername("Admin")).willReturn(user1);
        Double bmi = userService.calculateBMI(user1);
        int caloricDemand = userService.calculateCaloricDemand(user1);

        //when
        Map<String, Double> result = userService.giveBMIAndCaloricDemandInformation("Admin");

        //then
        assertAll(
                () -> assertThat(result.get("Your BMI is : "), equalTo(bmi)),
                () -> assertThat(result.get("Your caloric demand is :"), equalTo((double) caloricDemand)),
                () -> assertThat(result.get("To gain about 0.5kg per week, eat about"), equalTo((double) caloricDemand + 500))
        );
    }
}