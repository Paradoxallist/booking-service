package bookingservice.service.payment;

import bookingservice.dto.payment.CreatePaymentRequestDto;
import bookingservice.dto.payment.PaymentResponseDto;
import bookingservice.exception.EntityNotFoundException;
import bookingservice.mapper.PaymentMapper;
import bookingservice.model.Accommodation;
import bookingservice.model.Booking;
import bookingservice.model.Payment;
import bookingservice.model.User;
import bookingservice.repository.BookingRepository;
import bookingservice.repository.PaymentRepository;
import bookingservice.service.telegram.NotificationService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URL;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String DOMAIN = "http://localhost:8080";
    private static final String SUCCESS_URL = "/payments/success?sessionId={CHECKOUT_SESSION_ID}";
    private static final String CANCEL_URL = "/payments/cancel?sessionId={CHECKOUT_SESSION_ID}";
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;

    @Value("${stripe.apiKey}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    @Override
    public List<PaymentResponseDto> getUserPayments (Long userId, Pageable pageable) {
        return paymentRepository.findAllByBookingUserId(userId, pageable).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PaymentResponseDto createPaymentSession(CreatePaymentRequestDto request) {
        Optional<Payment> paymentFromDb = paymentRepository.findAllByBookingId(request.getBookingId())
                .stream()
                .filter(p -> p.getStatus() != Payment.Status.CANCELED)
                .findFirst();

        if (paymentFromDb.isPresent()) {
            Payment payment = paymentFromDb.get();
            if (payment.getStatus() == Payment.Status.PAID) {
                throw new EntityNotFoundException("This booking has been paid");
            }
            if (payment.getStatus() == Payment.Status.PENDING) {
                return paymentMapper.toDto(payment);
            }
        }

        Payment payment = new Payment();
        Booking booking = bookingRepository
                .findById(request.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find booking by id "
                        + request.getBookingId()));
        payment.setBooking(booking);

        Accommodation accommodation = booking.getAccommodation();
        BigDecimal dailyFee = accommodation.getDailyRate();
        LocalDate checkInDate = booking.getCheckInDate();
        LocalDate checkOutDate = booking.getCheckOutDate();
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        BigDecimal amountToPay = dailyFee.multiply(BigDecimal.valueOf(days));

        payment.setAmountToPay(amountToPay);
        payment.setStatus(Payment.Status.PENDING);
        payment = checkout(accommodation, payment);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    private Payment checkout(Accommodation accommodation, Payment payment) {
        SessionCreateParams.Builder builder = new
                SessionCreateParams.Builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setExpiresAt(Instant.now().plus(31, ChronoUnit.MINUTES)
                        .getEpochSecond())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(payment.getAmountToPay().longValue() * 100L)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData
                                                .builder()
                                                .setName("Booking " + accommodation.getLocation())
                                                .build())
                                .build()
                        ).setQuantity(1L)
                        .build()
                ).setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(DOMAIN + SUCCESS_URL)
                .setCancelUrl(DOMAIN + CANCEL_URL);
        Session session;
        try {
            session = Session.create(builder.build());
            payment.setSessionId(session.getId());
            payment.setSessionUrl(new URL(session.getUrl()));
        } catch (StripeException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return payment;
    }

    @Transactional
    @Override
    public PaymentResponseDto getSuccessfulPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cannot find session by id " + sessionId));
        payment.setStatus(Payment.Status.PAID);
        payment.getBooking().setStatus(Booking.Status.CONFIRMED);
        notificationService.sendMessageAboutSuccessPayment(payment, payment.getBooking().getAccommodation());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponseDto getCancelledPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cannot find session by id " + sessionId));
        payment.setStatus(Payment.Status.CANCELED);
        payment.getBooking().setStatus(Booking.Status.CANCELED);
        notificationService.sendMessageAboutCanceledPayment(payment, payment.getBooking().getAccommodation());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }
}
