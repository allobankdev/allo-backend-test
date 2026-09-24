package com.example.tech.splitbiller.controller;

import com.example.tech.splitbiller.model.response.BaseResponse;
import com.example.tech.splitbiller.model.response.DetailSettlementResponse;
import com.example.tech.splitbiller.model.response.SettlementResponse;
import com.example.tech.splitbiller.service.SettlementCalculatorService;
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
@RequestMapping(value = Constants.API_PATH + Constants.SETTLEMENT_PATH)
@Validated
public class SettlementController {

    private final SettlementCalculatorService settlementCalculatorService;

    public SettlementController(SettlementCalculatorService settlementCalculatorService) {
        this.settlementCalculatorService = settlementCalculatorService;
    }

    @GetMapping(value = Constants.GET_PATH + "/{groupId}")
    public ResponseEntity<BaseResponse<DetailSettlementResponse>> getSettlementGroup(@PathVariable("groupId") String groupId) {
        DetailSettlementResponse response = settlementCalculatorService.calculate(groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<DetailSettlementResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }
}
