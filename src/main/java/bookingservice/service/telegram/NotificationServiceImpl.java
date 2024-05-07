package bookingservice.service.telegram;

import bookingservice.model.Accommodation;
import bookingservice.model.Booking;
import bookingservice.model.Payment;
import bookingservice.telegram.BookingBot;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final BookingBot bookingBot;

    @Override
    public void sendMessageAboutCreatedBooking(Booking booking) {
        String accommodationInfo = formatAccommodationInfo(booking.getAccommodation());
        String dateInfo = formatDate(booking.getCheckInDate(),
                booking.getCheckOutDate());
        String name = "Sup, %s ".formatted(booking.getUser().getFirstName());
        String message = name + System.lineSeparator()
                + "You booking accommodation: " + System.lineSeparator()
                + accommodationInfo + System.lineSeparator()
                + dateInfo;
        bookingBot.sendMessage(message);
    }

    @Override
    public void sendMessageAboutSuccessPayment(Payment payment, Accommodation accommodation) {
        String message = """
                Payment succeed: 
                %s,
                Total: %sUSD
                """.formatted(formatAccommodationInfo(accommodation), payment.getAmountToPay());
        bookingBot.sendMessage(message);
    }

    @Override
    public void sendMessageAboutCanceledPayment(Payment payment, Accommodation accommodation) {
        String message = """
                Your payment for the accommodation: 
                %s
                is failed.
                Please, try again
                
                Sum to pay: %sUSD
                """.formatted(formatAccommodationInfo(accommodation),
                payment.getAmountToPay());
        bookingBot.sendMessage(message);
    }

    public String formatAccommodationInfo(Accommodation accommodation) {
        return """
                location ➡ %s,
                dailyRate ➡ %s,
                type ➡ %s"""
                .formatted(accommodation.getLocation(), accommodation.getDailyRate(), accommodation.getType().name());

    }

    private String formatDate(LocalDate CheckInDate, LocalDate CheckOutDate) {
        String formattedCheckInDate = CheckInDate.format(formatter);
        String formattedCheckOutDate = CheckOutDate.format(formatter);
        return """
                Your booking day and time is : %s,
                you should leave accommodation on %s.
                If you don't leave accommodation on time, you will be fined
                """.formatted(formattedCheckInDate, formattedCheckOutDate);
    }
}

