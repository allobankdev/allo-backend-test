package com.example.tech.splitbiller.controller;

import com.example.tech.splitbiller.model.response.BaseResponse;
import com.example.tech.splitbiller.model.response.ExpenseResponse;
import com.example.tech.splitbiller.model.response.PersonBalanceResponse;
import com.example.tech.splitbiller.service.BalanceCalculatorService;
import com.example.tech.splitbiller.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = Constants.API_PATH + Constants.BALANCE_PATH)
@Validated
public class BalanceController {

    private final BalanceCalculatorService balanceCalculatorService;

    public BalanceController(BalanceCalculatorService balanceCalculatorService) {
        this.balanceCalculatorService = balanceCalculatorService;
    }

    @GetMapping(value = Constants.GET_PATH + "/{groupId}")
    public ResponseEntity<BaseResponse<List<PersonBalanceResponse>>> getBalances(@PathVariable("groupId") String groupId) {
        List<PersonBalanceResponse> response = balanceCalculatorService.calculate(groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<List<PersonBalanceResponse>>builder().success(Boolean.TRUE).message("success").data(response).build());
    }
}
