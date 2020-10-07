package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.user.*;
import codebuddies.MealooApp.exceptions.EntityAlreadyFoundException;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import codebuddies.MealooApp.repositories.RoleRepository;

import codebuddies.MealooApp.security.jwt.JwtUtils;
import codebuddies.MealooApp.security.request.LoginRequest;
import codebuddies.MealooApp.security.request.SignupRequest;
import codebuddies.MealooApp.security.response.JwtResponse;
import codebuddies.MealooApp.security.response.MessageResponse;
import codebuddies.MealooApp.security.services.UserDetailsImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder encoder;

    private final MealooUserRepository userRepository;

    private final RoleRepository roleRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils, MealooUserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = createUsernamePasswordAuthenticationToken(loginRequest);

        setAuthentication(authentication);

        String jwt = generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = createNamesOfRoles(userDetails);

        return  new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public Authentication createUsernamePasswordAuthenticationToken(LoginRequest loginRequest){
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    public void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String generateJwtToken(Authentication authentication){
        return jwtUtils.generateJwtToken(authentication);
    }

    public List<String> createNamesOfRoles(UserDetails userDetails){

         return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public MessageResponse registerUser(SignupRequest signUpRequest) {

        validateRequest(signUpRequest);

        MealooUser user = createNewUserAccount(signUpRequest);

        assignUserRole(user);

        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }


    public void validateRequest(SignupRequest signUpRequest) {

        String username = signUpRequest.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new EntityAlreadyFoundException(username);
        }

        String email = signUpRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyFoundException(email);
        }
    }

    public MealooUser createNewUserAccount(SignupRequest signUpRequest) {

         return new MealooUser(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(),
                new NutritionSettings(0),
                new MealooUserDetails(0, 0, 0, Sex.MALE, PhysicalActivity.NONE));
    }

    public void assignUserRole(MealooUser user){
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(roleRepository.findByName(MealooUserRole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role USER")));
        user.setRoles(defaultRoles);
    }

}
