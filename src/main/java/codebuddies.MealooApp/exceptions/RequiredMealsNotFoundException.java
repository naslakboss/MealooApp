package codebuddies.MealooApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RequiredMealsNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RequiredMealsNotFoundException(String entity){
        super("Sorry, database does not contain required meals." +
                " Try to add new meals or create your own diary manually");
    }
}
