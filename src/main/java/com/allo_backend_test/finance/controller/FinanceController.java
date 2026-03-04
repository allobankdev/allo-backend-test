
package com.allo_backend_test.finance.controller;

import com.allo_backend_test.finance.Utils.Const;
import com.allo_backend_test.finance.service.FinanceDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataStore store;

    @GetMapping("/{resourceType}")
    public Object getData(@PathVariable String resourceType) {
        Object result = store.getData(resourceType);
        if (result == null) {
            throw new IllegalArgumentException(Const.INVALID_RESOURCE_TYPE);
        }
        return Collections.singletonList(result);
    }
}
