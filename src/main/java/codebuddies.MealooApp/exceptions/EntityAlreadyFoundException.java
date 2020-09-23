package codebuddies.MealooApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityAlreadyFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityAlreadyFoundException(String entity){
        super(entity + " already exists in the database");
    }
}
