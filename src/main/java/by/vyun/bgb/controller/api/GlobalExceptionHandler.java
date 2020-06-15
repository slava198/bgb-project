package by.vyun.bgb.controller.api;

import by.vyun.bgb.dto.error.ErrorDto;
import by.vyun.bgb.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleGlobalException(Exception exception) {

        LOGGER.error("Global Exception caught", exception);

        return ErrorDto.builder().message("Internal exception").build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto handleResourceNotFoundException(ResourceNotFoundException exception) {

        LOGGER.error("Resource Not Found Exception caught", exception);

        return ErrorDto.builder().message(exception.getMessage()).build();
    }
}
