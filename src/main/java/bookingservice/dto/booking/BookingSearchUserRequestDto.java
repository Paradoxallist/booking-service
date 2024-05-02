package bookingservice.dto.booking;

import bookingservice.model.Booking;
import bookingservice.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingSearchUserRequestDto {
    @NotNull
    private Long userId;
    @ValueOfEnum(enumClass = Booking.Status.class)
    private String status;
}
