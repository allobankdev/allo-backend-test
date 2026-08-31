package com.allobank.splitbill.service;

import com.allobank.splitbill.domain.entity.BillGroup;
import com.allobank.splitbill.domain.entity.Participant;
import com.allobank.splitbill.domain.entity.PaymentRecord;
import com.allobank.splitbill.dto.request.RecordPaymentRequest;
import com.allobank.splitbill.dto.response.ParticipantResponse;
import com.allobank.splitbill.dto.response.PaymentRecordResponse;
import com.allobank.splitbill.exception.InvalidExpenseException;
import com.allobank.splitbill.repository.ParticipantRepository;
import com.allobank.splitbill.repository.PaymentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRecordRepository paymentRecordRepository;
    private final ParticipantRepository participantRepository;
    private final GroupService groupService;

    @Transactional
    public PaymentRecordResponse recordPayment(Long groupId, RecordPaymentRequest request) {
        BillGroup group = groupService.getGroupEntity(groupId);

        if (request.getFromParticipantId().equals(request.getToParticipantId())) {
            throw new InvalidExpenseException("Payer and recipient cannot be the same participant");
        }

        Participant fromParticipant = participantRepository.findByIdAndGroupId(request.getFromParticipantId(), groupId)
                .orElseThrow(() -> new InvalidExpenseException("Payer ID " + request.getFromParticipantId() + " not found in group"));

        Participant toParticipant = participantRepository.findByIdAndGroupId(request.getToParticipantId(), groupId)
                .orElseThrow(() -> new InvalidExpenseException("Recipient ID " + request.getToParticipantId() + " not found in group"));

        BigDecimal amount = request.getAmount().setScale(2, RoundingMode.HALF_UP);

        PaymentRecord paymentRecord = PaymentRecord.builder()
                .group(group)
                .fromParticipant(fromParticipant)
                .toParticipant(toParticipant)
                .amount(amount)
                .notes(request.getNotes() != null ? request.getNotes().trim() : null)
                .build();

        PaymentRecord saved = paymentRecordRepository.save(paymentRecord);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PaymentRecordResponse> getPaymentsByGroup(Long groupId) {
        groupService.getGroupEntity(groupId);
        return paymentRecordRepository.findByGroupId(groupId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentRecordResponse mapToResponse(PaymentRecord record) {
        return PaymentRecordResponse.builder()
                .id(record.getId())
                .fromParticipant(ParticipantResponse.builder()
                        .id(record.getFromParticipant().getId())
                        .name(record.getFromParticipant().getName())
                        .build())
                .toParticipant(ParticipantResponse.builder()
                        .id(record.getToParticipant().getId())
                        .name(record.getToParticipant().getName())
                        .build())
                .amount(record.getAmount())
                .paidAt(record.getPaidAt())
                .notes(record.getNotes())
                .build();
    }
}
