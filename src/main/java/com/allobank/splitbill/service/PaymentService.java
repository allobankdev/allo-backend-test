package com.allobank.splitbill.service;

import com.allobank.splitbill.api.dto.request.CreatePaymentRequest;
import com.allobank.splitbill.api.dto.response.PaymentResponse;
import com.allobank.splitbill.domain.PaymentType;
import com.allobank.splitbill.persistence.BillRepository;
import com.allobank.splitbill.persistence.BillRepository.ParticipantRow;
import com.allobank.splitbill.persistence.BillRepository.PaymentRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final BillRepository repository;
    private final SettlementService settlementService;

    public PaymentService(BillRepository repository, SettlementService settlementService) {
        this.repository = repository;
        this.settlementService = settlementService;
    }

    @Transactional
    public PaymentResponse createPayment(UUID groupId, CreatePaymentRequest request) {
        if (request == null || request.payer() == null || request.receiver() == null) {
            throw new IllegalArgumentException("payer and receiver are required");
        }
        SettlementService.GroupSettlement settlement = settlementService.calculate(groupId);
        Map<String, ParticipantRow> participantsByUsername = settlement.participants().stream()
                .collect(Collectors.toMap(
                        participant -> UsernamePolicy.normalize(participant.username()),
                        participant -> participant));
        ParticipantRow payer = resolveParticipant(request.payer(), "payer", settlement.participants(),
                participantsByUsername);
        ParticipantRow receiver = resolveParticipant(request.receiver(), "receiver", settlement.participants(),
                participantsByUsername);
        if (payer.id().equals(receiver.id())) {
            throw new IllegalArgumentException("payer and receiver must be different participants");
        }
        BigDecimal amount = normalizeAmount(request.amount());

        BigDecimal payerBalance = settlement.balances().get(payer.id());
        BigDecimal receiverBalance = settlement.balances().get(receiver.id());
        if (payerBalance.signum() >= 0 || receiverBalance.signum() <= 0) {
            throw new ConflictException("payment must go from a current debtor to a current creditor");
        }
        BigDecimal maximumPayment = payerBalance.negate().min(receiverBalance);
        if (amount.compareTo(maximumPayment) > 0) {
            throw new ConflictException("payment exceeds the current outstanding amount of " + maximumPayment);
        }

        PaymentRow payment = new PaymentRow(UUID.randomUUID(), groupId, payer.id(),
                receiver.id(), amount, PaymentType.PAYMENT, null, Instant.now());
        repository.insertPayment(payment);
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPayments(UUID groupId) {
        repository.findGroup(groupId).orElseThrow(() -> new NotFoundException("group not found"));
        return repository.findPayments(groupId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public PaymentResponse reversePayment(UUID groupId, UUID paymentId) {
        repository.findGroup(groupId).orElseThrow(() -> new NotFoundException("group not found"));
        PaymentRow original = repository.findPayment(groupId, paymentId)
                .orElseThrow(() -> new NotFoundException("payment not found"));
        if (original.type() != PaymentType.PAYMENT) {
            throw new ConflictException("a reversal entry cannot be reversed");
        }
        if (repository.findReversalFor(original.id()).isPresent()) {
            throw new ConflictException("payment has already been reversed");
        }

        PaymentRow reversal = new PaymentRow(UUID.randomUUID(), groupId, original.receiverId(),
                original.payerId(), original.amount(), PaymentType.REVERSAL, original.id(), Instant.now());
        repository.insertPayment(reversal);
        return toResponse(reversal);
    }

    private BigDecimal normalizeAmount(BigDecimal value) {
        if (value == null || value.signum() <= 0 || value.scale() > 2) {
            throw new IllegalArgumentException("payment amount must be positive with at most two decimal places");
        }
        return value.setScale(2, RoundingMode.UNNECESSARY);
    }

    private PaymentResponse toResponse(PaymentRow payment) {
        return new PaymentResponse(payment.id(), payment.groupId(), payment.payerId(),
                payment.receiverId(), payment.amount(), payment.type(), payment.reversalOf(),
                payment.createdAt());
    }

    private ParticipantRow resolveParticipant(String value, String field, List<ParticipantRow> participants,
                                              Map<String, ParticipantRow> participantsByUsername) {
        String normalizedUsername = null;
        try {
            normalizedUsername = UsernamePolicy.normalize(value);
        } catch (IllegalArgumentException ignored) {
            // UUIDs remain accepted for existing API clients; new requests should use usernames.
        }
        if (normalizedUsername != null) {
            ParticipantRow participant = participantsByUsername.get(normalizedUsername);
            if (participant == null) {
                throw new IllegalArgumentException(field + " must be a participant in this group");
            }
            return participant;
        }
        UUID id;
        try {
            id = UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(field + " must be a group participant username");
        }
        return participants.stream().filter(participant -> participant.id().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(field + " must be a participant in this group"));
    }
}
