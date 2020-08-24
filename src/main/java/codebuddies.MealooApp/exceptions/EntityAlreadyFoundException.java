package codebuddies.MealooApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyFoundException extends RuntimeException {
    public EntityAlreadyFoundException(String entityName) {
        super(entityName + " with given parameters are already exist in database");
    }
}