package bookingservice.exception;

public class DateValidationException  extends RuntimeException {
    public DateValidationException(String message) {
        super(message);
    }
}
