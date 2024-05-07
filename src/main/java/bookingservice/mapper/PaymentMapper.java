package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.payment.PaymentResponseDto;
import bookingservice.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {BookingMapper.class})
public interface PaymentMapper {
    @Mapping(target = "bookingId",
            source = "booking",
            qualifiedByName = "idByBooking")
    PaymentResponseDto toDto(Payment payment);
}
