package com.allobank.splitbill.api;

import com.allobank.splitbill.api.dto.request.CreatePaymentRequest;
import com.allobank.splitbill.api.dto.response.PaymentResponse;
import com.allobank.splitbill.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable UUID groupId,
                                                          @Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = service.createPayment(groupId, request);
        return ResponseEntity.created(URI.create("/api/groups/" + groupId + "/payments/" + response.id()))
                .body(response);
    }

    @GetMapping
    public List<PaymentResponse> getPayments(@PathVariable UUID groupId) {
        return service.getPayments(groupId);
    }

    @PostMapping("/{paymentId}/reversal")
    public ResponseEntity<PaymentResponse> reversePayment(@PathVariable UUID groupId,
                                                           @PathVariable UUID paymentId) {
        PaymentResponse response = service.reversePayment(groupId, paymentId);
        return ResponseEntity.created(URI.create("/api/groups/" + groupId + "/payments/" + response.id()))
                .body(response);
    }
}
