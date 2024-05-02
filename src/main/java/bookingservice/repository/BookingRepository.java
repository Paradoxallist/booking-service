package bookingservice.repository;

import bookingservice.model.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserIdAndStatus(Long userId, Booking.Status status);

    @Query("SELECT b FROM Booking b JOIN FETCH b.accommodation JOIN FETCH b.user WHERE b.id = :id")
    Booking findByIdWithDetails(@Param("id") Long id);
}
