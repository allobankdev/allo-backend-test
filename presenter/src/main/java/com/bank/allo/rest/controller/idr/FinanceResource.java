package com.bank.allo.rest.controller.idr;

import com.bank.allo.rest.entity.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public interface FinanceResource {

    @GetMapping("/{resourceType}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<?> getFinanceData(@PathVariable String resourceType);
}
