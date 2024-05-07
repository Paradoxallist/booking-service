package bookingservice.service.telegram;

import bookingservice.model.Accommodation;
import bookingservice.model.Booking;
import bookingservice.model.Payment;

public interface NotificationService {

    void sendMessageAboutCreatedBooking(Booking booking);

    void sendMessageAboutSuccessPayment(Payment payment, Accommodation accommodation);

    void sendMessageAboutCanceledPayment(Payment payment, Accommodation accommodation);

}
