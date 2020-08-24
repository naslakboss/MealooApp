package codebuddies.MealooApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MealIsNeededException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MealIsNeededException(String message){
        super(message);
    }
}
