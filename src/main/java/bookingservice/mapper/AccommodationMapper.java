package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.accommodations.AccommodationDto;
import bookingservice.dto.accommodations.CreateAccommodationRequestDto;
import bookingservice.dto.accommodations.UpdateAccommodationRequestDto;
import bookingservice.dto.user.UpdateUserRequestDto;
import bookingservice.model.Accommodation;
import bookingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    AccommodationDto toDto(Accommodation accommodation);

    Accommodation toModel(CreateAccommodationRequestDto requestDto);

    void update(@MappingTarget Accommodation accommodation, UpdateAccommodationRequestDto requestDto);
}
