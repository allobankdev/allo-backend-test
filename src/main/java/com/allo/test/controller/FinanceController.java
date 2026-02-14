package com.allo.test.controller;

import com.allo.test.constant.GenericConstant;
import com.allo.test.dto.response.BaseResponseDto;
import com.allo.test.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;


    @GetMapping ("/data/{resourceType}")
    public ResponseEntity<BaseResponseDto> fetchData(
            @PathVariable String resourceType
    ){
        return new ResponseEntity<>(
                new BaseResponseDto(
                        HttpStatus.OK.value(),
                        GenericConstant.RESPONSE_OK,
                        resourceType,
                        null,
                        null,
                        financeService.fetchFrankfurtData(resourceType)
                ),
                HttpStatus.OK
        );
    }

}
