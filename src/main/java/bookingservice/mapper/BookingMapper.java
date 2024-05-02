package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.booking.BookingDto;
import bookingservice.dto.booking.CreateBookingRequestDto;
import bookingservice.dto.booking.UpdateBookingRequestDto;
import bookingservice.model.Booking;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class, AccommodationMapper.class})
public interface BookingMapper {
    @Mapping(target = "userId",
            source = "user",
            qualifiedByName = "idByUser")
    @Mapping(target = "accommodationId",
            source = "accommodation",
            qualifiedByName = "idByAccommodation")
    BookingDto toDto(Booking booking);

    List<BookingDto> toDtoList(List<Booking> books);

    Booking toModel(CreateBookingRequestDto requestDto);

    void update(@MappingTarget Booking booking,
                UpdateBookingRequestDto requestDto);
}
