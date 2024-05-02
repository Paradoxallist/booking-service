package bookingservice.service.booking;

import bookingservice.dto.booking.BookingDto;
import bookingservice.dto.booking.BookingSearchUserRequestDto;
import bookingservice.dto.booking.CreateBookingRequestDto;
import bookingservice.dto.booking.UpdateBookingRequestDto;
import bookingservice.exception.AccessLevelException;
import bookingservice.exception.EntityNotFoundException;
import bookingservice.mapper.BookingMapper;
import bookingservice.model.Accommodation;
import bookingservice.model.Booking;
import bookingservice.model.Role;
import bookingservice.model.User;
import bookingservice.repository.AccommodationRepository;
import bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final AccommodationRepository accommodationRepository;

    @Override
    public List<BookingDto> search(BookingSearchUserRequestDto searchParameters) {
        Booking.Status status = Booking.Status.valueOf(searchParameters.getStatus());
        return bookingMapper.toDtoList(
                bookingRepository.findByUserIdAndStatus(
                        searchParameters.getUserId(), status));
    }

    @Override
    public List<BookingDto> getUserBooking(User principal) {
        List<Booking> bookings = bookingRepository.findByUserId(principal.getId());
        return bookingMapper.toDtoList(bookings);
    }

    @Override
    @Transactional
    public BookingDto getById(Long id) {
        Booking booking =
                bookingRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Can't find booking with id: " + id));
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto update(User principal, Long bookingId, UpdateBookingRequestDto requestDto) {
        Booking booking =
                bookingRepository.findById(bookingId).orElseThrow(
                    () -> new EntityNotFoundException("Can't find booking with id: " + bookingId));

        if (getAccess(principal, booking)) {
            bookingMapper.update(booking, requestDto);
            return bookingMapper.toDto(bookingRepository.save(booking));
        }
        return null;
    }

    @Override
    @Transactional
    public BookingDto save(User principal, CreateBookingRequestDto requestDto) {
        Accommodation accommodation =
                accommodationRepository.findById(requestDto.getAccommodationId()).orElseThrow(
                    () -> new EntityNotFoundException("Can't find accommodation with id: " + requestDto.getAccommodationId()));
        Booking newBooking = bookingMapper.toModel(requestDto);
        newBooking.setUser(principal);
        newBooking.setAccommodation(accommodation);
        return bookingMapper.toDto(bookingRepository.save(newBooking));
    }

    @Override
    @Transactional
    public void delete(User principal, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new EntityNotFoundException("Can't find booking with id: " + bookingId));

        if (getAccess(principal, booking)) {
            bookingRepository.deleteById(bookingId);
        }
    }

    private boolean getAccess(User principal, Booking booking) {
        boolean access =
                principal.getRoles()
                        .stream().anyMatch(role -> role.getName() == Role.RoleName.ADMIN)
                || booking.getUser().getEmail().equals(principal.getEmail());
        if (!access) {
            throw new AccessLevelException("User : "
                    + principal.getEmail()
                    + " don't have access to booking with id: " + booking.getId());
        }
        return true;
    }
}
