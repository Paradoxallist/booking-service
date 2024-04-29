package bookingservice.dto.user;

import bookingservice.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password", second = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotEmpty
    @Email
    @Length(max = 255)
    private String email;
    @NotEmpty
    @Length(min = 8, max = 255)
    private String password;
    @NotEmpty
    @Length(min = 8, max = 255)
    private String repeatPassword;
    @NotEmpty
    @Length(max = 255)
    private String firstName;
    @NotEmpty
    @Length(max = 255)
    private String lastName;
}
