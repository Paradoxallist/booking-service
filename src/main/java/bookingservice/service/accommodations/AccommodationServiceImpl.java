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
                () -> new EntityNotFoundException("Can't find accommodation with id: " + id));

        if (getAccess(principal, accommodation)) {
            accommodationRepository.deleteById(id);
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
                () -> new EntityNotFoundException("Can't find accommodation with id: " + id));

        if (getAccess(principal, accommodation)) {
            accommodationMapper.update(accommodation, requestDto);
            return accommodationMapper.toDto(accommodationRepository.save(accommodation));
        }
        return null;
    }

    private boolean getAccess(User principal, Accommodation accommodation) {
        boolean access =
                principal.getRoles().stream()
                        .anyMatch(role -> role.getName() == Role.RoleName.ROLE_MANAGER)
                || accommodation.getOwner().getEmail().equals(principal.getEmail());
        if (!access) {
            throw new AccessLevelException("User : "
                    + principal.getEmail()
                    + " don't have access to accommodation with id: " + accommodation.getId());
        }
        return true;
    }
}

