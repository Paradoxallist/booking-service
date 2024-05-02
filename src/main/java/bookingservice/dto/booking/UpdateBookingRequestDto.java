package bookingservice.dto.booking;

import bookingservice.model.Booking;
import bookingservice.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateBookingRequestDto {
    @ValueOfEnum(enumClass = Booking.Status.class)
    private String status;
    @NotNull
    private LocalDate checkInDate;
    @NotNull
    private LocalDate checkOutDate;
}
