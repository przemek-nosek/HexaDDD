package pl.java.user.application.out.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.java.user.application.out.client.feign.exception.FeignBadRequestException;
import pl.java.user.application.out.client.feign.exception.FeignNotFoundException;
import pl.java.user.application.out.exception.message.ErrorMessage;
import pl.java.user.domain.exception.ExternalApiFieldValidationException;
import pl.java.user.domain.exception.ExternalApiReturnNullException;
import pl.java.user.domain.exception.ZeroFollowersException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(FeignBadRequestException.class)
    ResponseEntity<ErrorMessage> handleFeignBadRequestException(FeignBadRequestException ex) {
        return new ResponseEntity<>(getErrorMessage(BAD_REQUEST, ex.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(FeignNotFoundException.class)
    ResponseEntity<ErrorMessage> handleFeignNotFoundException(FeignNotFoundException ex) {
        return new ResponseEntity<>(getErrorMessage(NOT_FOUND, ex.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler({ZeroFollowersException.class, ExternalApiFieldValidationException.class, ExternalApiReturnNullException.class})
    ResponseEntity<ErrorMessage> handleUnprocessableEntityException(RuntimeException ex) {
        return new ResponseEntity<>(getErrorMessage(UNPROCESSABLE_ENTITY, ex.getMessage()), UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<ErrorMessage> handleOtherExceptions(Throwable ex) {
        return new ResponseEntity<>(getErrorMessage(INTERNAL_SERVER_ERROR, ex.getMessage()), INTERNAL_SERVER_ERROR);
    }

    private ErrorMessage getErrorMessage(HttpStatus httpStatus, String message) {
        return new ErrorMessage(httpStatus, httpStatus.value(), LocalDateTime.now(), message);
    }
}
