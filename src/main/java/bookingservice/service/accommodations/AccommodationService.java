package bookingservice.service.accommodations;

import bookingservice.dto.accommodations.AccommodationDto;
import bookingservice.dto.accommodations.CreateAccommodationRequestDto;
import bookingservice.dto.accommodations.UpdateAccommodationRequestDto;
import bookingservice.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    List<AccommodationDto> findAll(Pageable pageable);

    AccommodationDto getById(Long id);

    void delete(User principal, Long id);

    AccommodationDto save(User principal, CreateAccommodationRequestDto requestDto);

    AccommodationDto update(User principal, Long id, UpdateAccommodationRequestDto requestDto);
}
