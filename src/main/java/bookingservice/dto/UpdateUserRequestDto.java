package bookingservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateUserRequestDto {
    @NotEmpty
    @Length(max = 255)
    private String firstName;
    @NotEmpty
    @Length(max = 255)
    private String lastName;
}
