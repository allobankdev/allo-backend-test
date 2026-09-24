package com.example.tech.splitbiller.mapper;

import com.example.tech.splitbiller.entity.Payment;
import com.example.tech.splitbiller.model.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    public PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(payment.getId(),
                payment.getFromPerson().getId(),
                payment.getFromPerson().getName(),
                payment.getToPerson().getId(),
                payment.getToPerson().getName(),
                payment.getAmount(),
                payment.getPaidAt(),
                payment.getCreatedAt());
    }
}
