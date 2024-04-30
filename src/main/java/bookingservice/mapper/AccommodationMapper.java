package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.accommodations.AccommodationDto;
import bookingservice.dto.accommodations.CreateAccommodationRequestDto;
import bookingservice.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    AccommodationDto toDto(Accommodation accommodation);

    Accommodation toModel(CreateAccommodationRequestDto requestDto);

    void update(@MappingTarget Accommodation accommodation,
                CreateAccommodationRequestDto requestDto);
}
