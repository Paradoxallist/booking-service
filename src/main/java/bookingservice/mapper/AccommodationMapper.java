package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.accommodations.AccommodationDto;
import bookingservice.dto.accommodations.CreateAccommodationRequestDto;
import bookingservice.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class})
public interface AccommodationMapper {
    @Mapping(target = "ownerId", source = "owner", qualifiedByName = "idByUser")
    AccommodationDto toDto(Accommodation accommodation);

    Accommodation toModel(CreateAccommodationRequestDto requestDto);

    void update(@MappingTarget Accommodation accommodation,
                CreateAccommodationRequestDto requestDto);

    @Named("idByAccommodation")
    default Long idByUser(Accommodation accommodation) {
        return accommodation.getId();
    }
}
