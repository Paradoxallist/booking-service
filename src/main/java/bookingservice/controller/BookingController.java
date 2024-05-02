package bookingservice.controller;

import bookingservice.dto.booking.BookingDto;
import bookingservice.dto.booking.BookingSearchUserRequestDto;
import bookingservice.dto.booking.CreateBookingRequestDto;
import bookingservice.dto.booking.UpdateBookingRequestDto;
import bookingservice.model.User;
import bookingservice.service.booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accommodation booking",
        description = "Endpoints for managing booking")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Search for bookings",
            description = "Searching bookings by some parameters")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<BookingDto> search(BookingSearchUserRequestDto requestDto) {
        return bookingService.search(requestDto);
    }

    @GetMapping("/me")
    @Operation(summary = "Get user's booking",
            description = "Provides a list of user's booking")
    public List<BookingDto> getMyBooking(Authentication authentication) {
        return bookingService.getUserBooking((User) authentication.getPrincipal());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the booking by id",
            description = "Retrieves detailed information about a specific booking")
    public BookingDto getById(@PathVariable("id") Long id) {
        return bookingService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update booking by id",
            description = "Allows updates to booking details")
    public BookingDto updateBooking(Authentication authentication,
                                                @PathVariable("id") Long bookingId,
                                                @RequestBody @Valid UpdateBookingRequestDto requestDto
    ) {
        return bookingService.update(
                (User) authentication.getPrincipal(),
                bookingId,
                requestDto);
    }

    @PostMapping
    @Operation(summary = "Create a new booking")
    public BookingDto createBooking(
            Authentication authentication,
            @RequestBody @Valid CreateBookingRequestDto requestDto) {
        return bookingService.save((User) authentication.getPrincipal(),requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete the booking by id")
    public void deleteById(Authentication authentication,
                           @PathVariable("id") Long id) {
        bookingService.delete((User) authentication.getPrincipal(), id);
    }
}
