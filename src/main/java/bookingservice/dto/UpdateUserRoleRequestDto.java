package bookingservice.dto;

import bookingservice.model.Role;
import bookingservice.validation.ValueOfEnum;
import lombok.Data;

@Data
public class UpdateUserRoleRequestDto {
    @ValueOfEnum(enumClass = Role.RoleName.class)
    private String name;
}
