package com.athallah.finance.controller;

import com.athallah.finance.config.message.EnumMessagesKey;
import com.athallah.finance.service.FinanceService;
import com.athallah.finance.util.constant.ResourceType;
import com.athallah.finance.util.response.GlobalRespDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getData(@PathVariable ResourceType resourceType) {

        var data = financeService.getData(resourceType);

        var response = GlobalRespDto.successResponseBuilder()
                .data(data)
                .message(EnumMessagesKey.DATA_FETCHED_SUCCESS.getMessageKey())
                .build();

        return ResponseEntity.ok(response);
    }

}
