package bookingservice.dto.accommodations;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateAccommodationRequestDto {
    private String typeAccommodation;
    private String location;
    private String size;
    private String amenities;
    private BigDecimal dailyRate;
    private int availability;
}
