package com.example.tech.splitbiller.controller;

import com.example.tech.splitbiller.model.request.CreatePaymentRequest;
import com.example.tech.splitbiller.model.response.BaseResponse;
import com.example.tech.splitbiller.model.response.PaymentResponse;
import com.example.tech.splitbiller.service.PaymentService;
import com.example.tech.splitbiller.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = Constants.API_PATH + Constants.PAYMENT_PATH)
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping(value = Constants.GET_PATH + "/{groupId}")
    public ResponseEntity<BaseResponse<List<PaymentResponse>>> getGroupPayment(@PathVariable("groupId") String groupId) {
        List<PaymentResponse> response = paymentService.getGroupPayment(groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<List<PaymentResponse>>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @PostMapping(value = Constants.CREATE_PATH)
    public ResponseEntity<BaseResponse<PaymentResponse>> createPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                       @Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(idempotencyKey, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<PaymentResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }
}
