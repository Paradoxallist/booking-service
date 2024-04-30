package bookingservice.service.accommodations;

import bookingservice.dto.accommodations.AccommodationDto;
import bookingservice.dto.accommodations.CreateAccommodationRequestDto;
import bookingservice.exception.AccessLevelException;
import bookingservice.exception.EntityNotFoundException;
import bookingservice.mapper.AccommodationMapper;
import bookingservice.model.Accommodation;
import bookingservice.model.Role;
import bookingservice.model.User;
import bookingservice.repository.AccommodationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    public List<AccommodationDto> findAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccommodationDto getById(Long id) {
        return accommodationMapper.toDto(
                accommodationRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't find accommodation with id: " + id)
                ));
    }

    @Override
    @Transactional
    public void delete(User principal, Long id) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find accommodation with id: " + id)
        );

        if (principal.getRoles().stream().anyMatch(role -> role.getName() == Role.RoleName.ADMIN)) {
            accommodationRepository.deleteById(id);
        } else {
            if (accommodation.getOwner().getEmail().equals(principal.getEmail())) {
                accommodationRepository.deleteById(id);
            } else {
                throw new AccessLevelException("User : "
                        + principal.getEmail()
                        + " don't have access to accommodation with id: " + id);
            }
        }
    }

    @Override
    public AccommodationDto save(User principal, CreateAccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        accommodation.setOwner(principal);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    @Transactional
    public AccommodationDto update(User principal,
                                   Long id,
                                   CreateAccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find accommodation with id: " + id)
        );
        accommodationMapper.update(accommodation, requestDto);

        if (principal.getRoles().stream().anyMatch(role -> role.getName() == Role.RoleName.ADMIN)) {
            return accommodationMapper.toDto(accommodationRepository.save(accommodation));
        } else {
            if (accommodation.getOwner().getEmail().equals(principal.getEmail())) {
                return accommodationMapper.toDto(accommodationRepository.save(accommodation));
            } else {
                throw new AccessLevelException("User : "
                        + principal.getEmail()
                        + " don't have access to accommodation with id: " + id);
            }
        }
    }
}

