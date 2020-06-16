package by.vyun.bgb.exception;

import static java.lang.String.format;

public class ResourceDuplicateException extends RuntimeException {

        private static final String MESSAGE = "Resource of type '%s' with name '%s' is duplicated";

        public ResourceDuplicateException() {
        }

        public ResourceDuplicateException(String resourceType, Object resourceId) {
            super(format(MESSAGE, resourceType, resourceId));
        }

        public ResourceDuplicateException(String message) {
            super(message);
        }




}
