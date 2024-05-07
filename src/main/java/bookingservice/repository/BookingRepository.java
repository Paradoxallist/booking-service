package bookingservice.repository;

import bookingservice.model.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserIdAndStatus(Long userId, Booking.Status status);

    Optional<Booking> findBookingByStatusAndUserId(Booking.Status status, Long userId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b "
            + "WHERE (b.checkInDate BETWEEN :checkInDate AND :checkOutDate "
            + "OR b.checkOutDate BETWEEN :checkInDate AND :checkOutDate) "
            + "AND b.status IN ('PENDING', 'CONFIRMED') "
            + "AND b.accommodation.id = :accommodationId")
    boolean existsBookingForPeriodAndStatusAndAccommodation(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("accommodationId") Long accommodationId);
}
