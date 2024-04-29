package bookingservice.dto.accommodations;

import bookingservice.model.Accommodation;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class AccommodationDto {
    private Long id;
    private Accommodation.TypeAccommodation typeAccommodation;
    private String location;
    private String size;
    private String amenities;
    private BigDecimal dailyRate;
    private int availability;
    private Long ownerId;
}
