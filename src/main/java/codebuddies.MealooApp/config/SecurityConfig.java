package codebuddies.MealooApp.config;

import codebuddies.MealooApp.repositories.MealooUserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MealooUserRepository mealooUserRepository;

    public SecurityConfig(MealooUserRepository mealooUserRepository) {
        this.mealooUserRepository = mealooUserRepository;
    }

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
        auth.userDetailsService(mealooUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin().defaultSuccessUrl("/meals", true)
                .permitAll()
                .and().logout().logoutSuccessUrl("/login");
    }
}
