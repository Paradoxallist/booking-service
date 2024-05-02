package bookingservice.dto.booking;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateBookingRequestDto {
    private String status;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long accommodationId;
}
