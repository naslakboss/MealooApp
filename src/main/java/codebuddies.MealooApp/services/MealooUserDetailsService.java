package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.user.MealooUser;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.springframework.security.core.userdetails.User.*;

public class MealooUserDetailsService implements UserDetailsService {

    private MealooUserRepository mealooUserRepository;

    public MealooUserDetailsService(MealooUserRepository mealooUserRepository) {
        this.mealooUserRepository = mealooUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MealooUser mealooUser = mealooUserRepository.findByUsername(username);
        UserBuilder builder = null;
        if(mealooUser != null){
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(new BCryptPasswordEncoder().encode(mealooUser.getPassword()));
            builder.roles(mealooUser.getMealooUserRole().toString());
        }
        else{
            throw new UsernameNotFoundException(username);
        }
        return builder.build();
    }
}
