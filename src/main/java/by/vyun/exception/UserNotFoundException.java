package by.vyun.exception;



public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super(login);
    }

}