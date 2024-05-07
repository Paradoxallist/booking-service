package bookingservice.service.payment;

import bookingservice.dto.payment.CreatePaymentRequestDto;
import bookingservice.dto.payment.PaymentResponseDto;
import bookingservice.model.User;
import com.stripe.exception.StripeException;
import java.net.MalformedURLException;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    List<PaymentResponseDto> getUserPayments(Long userId, Pageable pageable);
    PaymentResponseDto createPaymentSession(CreatePaymentRequestDto request);
    PaymentResponseDto getSuccessfulPayment(String sessionId);
    PaymentResponseDto getCancelledPayment(String sessionId);
}
