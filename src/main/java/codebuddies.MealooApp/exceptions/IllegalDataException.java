package codebuddies.MealooApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalDataException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public IllegalDataException(String message){
        super(message);
    }
}
