package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import codebuddies.MealooApp.repositories.RoleRepository;
import codebuddies.MealooApp.security.jwt.JwtUtils;
import codebuddies.MealooApp.security.request.LoginRequest;
import codebuddies.MealooApp.security.request.SignupRequest;
import codebuddies.MealooApp.security.response.MessageResponse;
import codebuddies.MealooApp.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    @Mock
    RoleRepository roleRepository;

    @Mock
    MealooUserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    PasswordEncoder passwordEncoder;

    LoginRequest loginRequest;

    SignupRequest signupRequest;

    AuthService authService;

    @BeforeEach
    void setUp(){
        loginRequest = new LoginRequest();
        loginRequest.setUsername("Admin");
        loginRequest.setPassword("Password");

        signupRequest = new SignupRequest();
        signupRequest.setUsername("Jack");
        signupRequest.setPassword("SecretPass");
        signupRequest.setEmail("jack@mail.com");

        authService = new AuthService(authenticationManager, jwtUtils,
                userRepository, roleRepository, passwordEncoder);
    }


    @Test
    void shouldCreateUsernamePasswordAuthenticationToken() {
        //given + when
        Authentication result = authService.createUsernamePasswordAuthenticationToken(loginRequest);

        //then
        verify(authenticationManager, times(1)).authenticate(any());
    }


    @Test
    void shouldGenerateJwtToken() {
        //given
        given(jwtUtils.generateJwtToken(any())).willReturn("HardCodedToken");

        //when
        String result = authService.generateJwtToken(any(Authentication.class));
        //then

        assertAll(
                () -> verify(jwtUtils, times(1)).generateJwtToken(any()),
                () -> assertThat(result, is("HardCodedToken")));
    }

    @Test
    void shouldCreateNamesOfRoles() {
        //given
        SimpleGrantedAuthority user = new SimpleGrantedAuthority("USER");
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        UserDetails userDetails =
                new UserDetailsImpl(1L, "John", "Smith", "pass", List.of(user, admin));

        //when
        List<String> result = authService.createNamesOfRoles(userDetails);

        //then
        assertAll(
                () -> assertThat(result.size(), equalTo(2)),
                () -> assertThat(result.get(0), equalTo("USER")),
                () -> assertThat(result.get(1), equalTo("ADMIN"))
        );
    }

    @Test
    void shouldThrowEntityAlreadyFoundExceptionDuringCreatingUserAccountOfExistedName() {
        //given + when
        given(userRepository.existsByUsername("Jack")).willReturn(true);

        //then
        assertThrows(EntityAlreadyFoundException.class, () ->
                authService.validateRequest(signupRequest));
    }

    @Test
    void shouldThrowEntityAlreadyFoundExceptionDuringCreatingUserAccountOfExistedEmail() {
        //given + when
        given(userRepository.existsByEmail("jack@mail.com")).willReturn(true);

        //then
        assertThrows(EntityAlreadyFoundException.class, () ->
                authService.validateRequest(signupRequest));
    }

    @Test
    void shouldCreateNewUserAccountWithClearDataAndInformationFromSignUpRequest() {
        //given
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn("encodedPass");

        //when
        MealooUser createdUser = authService.createNewUserAccount(signupRequest);

        //then
        assertAll(
                () -> assertThat(createdUser.getUsername(), equalTo(signupRequest.getUsername())),
                () -> assertThat(createdUser.getPassword(), equalTo("encodedPass")),
                () -> assertThat(createdUser.getEmail(), equalTo(signupRequest.getEmail())),
                () -> assertThat(createdUser.getNutritionSettings().getDailyCaloricGoal(), equalTo( 0))

        );
    }

    @Test
    void shouldAssignUserRoleToNewlyCreatedAccount() {
        //given
        Role userRole = new Role(MealooUserRole.ROLE_USER);
        given(roleRepository.findByName(MealooUserRole.ROLE_USER)).willReturn(java.util.Optional.of(userRole));
        MealooUser user = new MealooUser(signupRequest.getUsername(), signupRequest.getPassword(),
                signupRequest.getEmail(), new NutritionSettings(0), new MealooUserDetails(0,0,0, Sex.MALE, PhysicalActivity.NONE));

        //when
        authService.assignUserRole(user);

        //then
        assertThat(user.getRoles(), hasItems(userRole));
    }

    @Test
    void shouldCreateUserAndReturnMessageIfDataIsCorrect(){
        //given
        given(userRepository.existsByUsername("Jack")).willReturn(false);
        given(userRepository.existsByEmail("jack@mail.com")).willReturn(false);
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn("encodedPass");
        Role userRole = new Role(MealooUserRole.ROLE_USER);
        given(roleRepository.findByName(MealooUserRole.ROLE_USER)).willReturn(java.util.Optional.of(userRole));

        //when
        MessageResponse result = authService.registerUser(signupRequest);

        //then
        assertAll(
                () -> verify(userRepository, times(1)).save(any()),
                () -> assertThat(result.getMessage(), equalTo("User registered successfully!")));
    }
}