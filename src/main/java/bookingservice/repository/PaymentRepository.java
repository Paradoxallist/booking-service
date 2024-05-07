package bookingservice.repository;

import bookingservice.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByBookingUserId(Long id, Pageable pageable);

    Optional<Payment> findBySessionId(String sessionId);

    List<Payment> findAllByBookingId(Long bookingId);
}
