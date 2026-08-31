package com.allobank.splitbill.controller;

import com.allobank.splitbill.dto.request.RecordPaymentRequest;
import com.allobank.splitbill.dto.response.PaymentRecordResponse;
import com.allobank.splitbill.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Endpoints for recording and listing direct settlement payments between participants")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Record a settlement payment", description = "Records a direct payment made from one participant to another to settle outstanding debts")
    public ResponseEntity<PaymentRecordResponse> recordPayment(
            @PathVariable Long groupId,
            @Valid @RequestBody RecordPaymentRequest request) {
        PaymentRecordResponse response = paymentService.recordPayment(groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List all recorded payments in a group", description = "Retrieves all direct settlement payments recorded for a group")
    public ResponseEntity<List<PaymentRecordResponse>> getPayments(@PathVariable Long groupId) {
        List<PaymentRecordResponse> payments = paymentService.getPaymentsByGroup(groupId);
        return ResponseEntity.ok(payments);
    }
}
