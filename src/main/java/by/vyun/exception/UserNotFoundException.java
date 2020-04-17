package by.vyun.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such User")  // 404
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super(login);
    }

}