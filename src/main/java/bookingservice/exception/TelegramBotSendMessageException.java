package bookingservice.exception;

public class TelegramBotSendMessageException extends RuntimeException {
    public TelegramBotSendMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
