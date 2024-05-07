package bookingservice.service.booking;

import bookingservice.dto.booking.BookingDto;
import bookingservice.dto.booking.BookingSearchUserRequestDto;
import bookingservice.dto.booking.CreateBookingRequestDto;
import bookingservice.dto.booking.UpdateBookingRequestDto;
import bookingservice.model.User;
import java.util.List;

public interface BookingService {
    List<BookingDto> search(BookingSearchUserRequestDto requestDto);

    List<BookingDto> getUserBooking(User principal);

    BookingDto getById(User principal, Long id);

    BookingDto update(User principal, Long bookingId, UpdateBookingRequestDto requestDto);

    BookingDto save(User principal, CreateBookingRequestDto requestDto);

    void delete(User principal, Long bookingId);
}
