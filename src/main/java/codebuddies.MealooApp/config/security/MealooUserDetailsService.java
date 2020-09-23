package codebuddies.MealooApp.config.security;

import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.*;

public class MealooUserDetailsService implements UserDetailsService {

    private MealooUserRepository mealooUserRepository;

    public MealooUserDetailsService(MealooUserRepository mealooUserRepository) {
        this.mealooUserRepository = mealooUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MealooUser> mealooUser = mealooUserRepository.findByUsername(username);
        UserBuilder builder = null;

        if(mealooUser.isPresent()){
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(new BCryptPasswordEncoder().encode(mealooUser.get().getPassword()));
            builder.roles(mealooUser.get().getMealooUserRole().toString());
        }
        else{
            throw new UsernameNotFoundException(username);
        }
        return builder.build();
    }
}
