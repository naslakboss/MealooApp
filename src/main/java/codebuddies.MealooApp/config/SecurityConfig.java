package codebuddies.MealooApp.config;

import codebuddies.MealooApp.entities.user.MealooUserDetails;
import codebuddies.MealooApp.entities.user.MealooUserRole;
import codebuddies.MealooApp.repositories.MealooUserRepository;
import codebuddies.MealooApp.services.MealooUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Access;
import java.util.Collections;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MealooUserRepository mealooUserRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MealooUserDetailsService mealooUserDetailsService(){
        return new MealooUserDetailsService(mealooUserRepository);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Uwierzytelnianie użytkownika -> najpierw UserDetailsService
        auth.userDetailsService(mealooUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/meal/**", "/users", "/client")
//                    .hasAnyRole(MealooUserRole.ADMIN.toString()
//                        , MealooUserRole.MODERATOR.toString()
//                            , MealooUserRole.USER.toString())
//                .antMatchers("/product/**")
//                    .hasAnyRole(MealooUserRole.ADMIN.toString()
//                        , MealooUserRole.MODERATOR.toString())
//                .antMatchers("/**")
//                    .hasRole(MealooUserRole.ADMIN.toString())

                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().logout();

    }
}
