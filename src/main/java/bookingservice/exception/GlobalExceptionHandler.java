package bookingservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP_VARIABLE = "timestamp";
    private static final String STATUS_VARIABLE = "status";
    private static final String ERRORS_VARIABLE = "errors";

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_VARIABLE, LocalDateTime.now());
        body.put(STATUS_VARIABLE, HttpStatus.BAD_REQUEST);
        List<String> errors = exception.getConstraintViolations().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put(ERRORS_VARIABLE, errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessage(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        return field + " " + message;

    }

    @ExceptionHandler({RegistrationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> passwordMatchException(
            Throwable exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_VARIABLE, LocalDateTime.now());
        body.put(STATUS_VARIABLE, HttpStatus.BAD_REQUEST);
        String errors = exception.getMessage();
        body.put(ERRORS_VARIABLE, errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
