package bookingservice.controller;

import bookingservice.dto.accommodations.AccommodationDto;
import bookingservice.dto.accommodations.CreateAccommodationRequestDto;
import bookingservice.model.User;
import bookingservice.service.accommodations.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accommodation management",
        description = "Endpoints for managing accommodation inventory")
@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    @GetMapping
    @Operation(summary = "Get all accommodations",
            description = "Provides a list of available accommodations")
    public List<AccommodationDto> getAll(Pageable pageable) {
        return accommodationService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the accommodations by id",
            description = "Retrieves detailed information about a specific accommodation")
    public AccommodationDto getById(@PathVariable("id") Long id) {
        return accommodationService.getById(id);
    }

    @PutMapping("/{id}")
    @PatchMapping("/{id}")
    @Operation(summary = "Update accommodation by id",
            description = "Allows updates to accommodation details, including inventory management")
    public AccommodationDto updateAccommodation(Authentication authentication,
                                  @PathVariable("id") Long id,
                                  @RequestBody @Valid CreateAccommodationRequestDto requestDto) {
        return accommodationService.update((User) authentication.getPrincipal(),id, requestDto);
    }

    @PostMapping
    @Operation(summary = "Create a new accommodations",
            description = "Create a new accommodations")
    public AccommodationDto createAccommodation(
            Authentication authentication,
            @RequestBody @Valid CreateAccommodationRequestDto requestDto) {
        return accommodationService.save((User) authentication.getPrincipal(),requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete the accommodation by id",
            description = "Delete the accommodation by id")
    public void deleteById(Authentication authentication,
                           @PathVariable("id") Long id) {
        accommodationService.delete((User) authentication.getPrincipal(), id);
    }
}
