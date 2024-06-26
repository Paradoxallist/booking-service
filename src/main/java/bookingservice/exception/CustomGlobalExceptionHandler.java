package bookingservice.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP_VARIABLE = "timestamp";
    private static final String STATUS_VARIABLE = "status";
    private static final String ERRORS_VARIABLE = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_VARIABLE, LocalDateTime.now());
        body.put(STATUS_VARIABLE, HttpStatus.BAD_REQUEST);
        body.put(ERRORS_VARIABLE,
                ex.getBindingResult().getAllErrors().stream()
                        .map(this::getErrorMessage)
                        .toList()
        );

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(
            {
                    RegistrationException.class,
                    EntityNotFoundException.class,
                    DateValidationException.class
            }
    )
    public ResponseEntity<Object> handleRegistrationException(
            RuntimeException exception
    ) {
        HttpStatus status = getStatus(exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_VARIABLE, LocalDateTime.now());
        body.put(STATUS_VARIABLE, status);
        body.put(ERRORS_VARIABLE, exception.getMessage());

        return ResponseEntity.status(status).body(body);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            return field + " " + message;
        }

        return error.getDefaultMessage();
    }

    private HttpStatus getStatus(RuntimeException exception) {
        HttpStatus status = null;

        if (exception instanceof RegistrationException) {
            status = HttpStatus.CONFLICT;
        } else if (exception instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return status;
    }
}
