package by.vyun.bgb.exception;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE = "No resource of type '%s' can be found with id '%s'";

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(format(MESSAGE, resourceType, resourceId));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
