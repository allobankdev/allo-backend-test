package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.entity.IdempotencyKey;
import com.example.tech.splitbiller.entity.Payment;
import com.example.tech.splitbiller.entity.Person;
import com.example.tech.splitbiller.exception.DataInvalidException;
import com.example.tech.splitbiller.exception.TransactionConflictException;
import com.example.tech.splitbiller.mapper.PaymentMapper;
import com.example.tech.splitbiller.model.request.CreatePaymentRequest;
import com.example.tech.splitbiller.model.response.PaymentResponse;
import com.example.tech.splitbiller.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final GroupService groupService;
    private final PersonService personService;
    private final GroupMemberService groupMemberService;
    private final IdempotencyKeyService idempotencyKeyService;
    private final ObjectMapper objectMapper;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository,
                          GroupService groupService,
                          PersonService personService,
                          GroupMemberService groupMemberService,
                          IdempotencyKeyService idempotencyKeyService,
                          ObjectMapper objectMapper,
                          PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.groupService = groupService;
        this.personService = personService;
        this.groupMemberService = groupMemberService;
        this.idempotencyKeyService = idempotencyKeyService;
        this.objectMapper = objectMapper;
        this.paymentMapper = paymentMapper;
    }

    public PaymentResponse createPayment(String idempotencyKey, CreatePaymentRequest request) {
        String hashedRequest = objectMapper.writeValueAsString(request);
        Optional<IdempotencyKey> find = idempotencyKeyService.findByIdempotencyKey(idempotencyKey);
        if(find.isPresent()) {
            IdempotencyKey idempotencyKeyObj = find.get();
            if(!idempotencyKeyObj.getHashRequest().equals(hashedRequest))
                throw new TransactionConflictException("Idempotency-Key was already used with a different request");

            String response = idempotencyKeyObj.getResponse();
            return objectMapper.readValue(response, PaymentResponse.class);
        }

        Group group = groupService.getActiveById(request.groupId());

        if (request.fromPersonId().equals(request.toPersonId())) {
            throw new DataInvalidException("A person cannot pay themselves");
        }

        Person fromPerson = personService.getById(request.fromPersonId());
        groupMemberService.validateMember(group, fromPerson);

        Person toPerson = personService.getById(request.toPersonId());
        groupMemberService.validateMember(group, toPerson);

        BigDecimal amount = request.amount().setScale(2, RoundingMode.UNNECESSARY);

        Payment payment = new Payment();
        payment.setGroup(group);
        payment.setFromPerson(fromPerson);
        payment.setToPerson(toPerson);
        payment.setAmount(amount);
        payment.setPaidAt(request.paidAt());
        Payment saved = paymentRepository.save(payment);

        PaymentResponse response = paymentMapper.toPaymentResponse(saved);
        idempotencyKeyService.save(idempotencyKey, hashedRequest, objectMapper.writeValueAsString(response));
        return response;
    }

    public List<PaymentResponse> getGroupPayment(String groupId) {
        return paymentRepository.findByGroupIdOrderByPaidAtDesc(groupId).stream().map(paymentMapper::toPaymentResponse).toList();
    }
}
