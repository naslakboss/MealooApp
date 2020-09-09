package codebuddies.MealooApp;

import codebuddies.MealooApp.entities.user.MealooUserRole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MealooAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealooAppApplication.class, args);
	}
}
