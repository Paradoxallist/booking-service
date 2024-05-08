package bookingservice.service.payment;

import bookingservice.dto.payment.CreatePaymentRequestDto;
import bookingservice.dto.payment.PaymentResponseDto;
import bookingservice.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    List<PaymentResponseDto> getUserPayments(Long userId, Pageable pageable);

    PaymentResponseDto createPaymentSession(User user, CreatePaymentRequestDto request);

    PaymentResponseDto getSuccessfulPayment(User user, Long paymentId);

    PaymentResponseDto getCancelledPayment(User user, Long paymentId);
}
