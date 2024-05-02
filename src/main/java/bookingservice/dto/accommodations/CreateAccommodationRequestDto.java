package bookingservice.dto.accommodations;

import bookingservice.model.Accommodation;
import bookingservice.validation.ValueOfEnum;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateAccommodationRequestDto {
    @ValueOfEnum(enumClass = Accommodation.Type.class)
    private String type;
    private String location;
    private String size;
    private String amenities;
    private BigDecimal dailyRate;
    private int availability;
}
