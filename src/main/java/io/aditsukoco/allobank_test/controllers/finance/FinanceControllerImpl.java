package io.aditsukoco.allobank_test.controllers.finance;

import io.aditsukoco.allobank_test.exceptions.BaseRestException;
import io.aditsukoco.allobank_test.models.enums.ResourceTypeEnum;
import io.aditsukoco.allobank_test.services.finance.FinanceServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FinanceControllerImpl implements FinanceControllerInterface {

    @Autowired private final FinanceServiceInterface financeService;

    @Override
    @GetMapping("/api/finance/data/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable(name = "resourceType") String resourceType) {
        try {
            ResourceTypeEnum resourceTypeEnum = ResourceTypeEnum.stringToEnum(resourceType);
            Object res = financeService.getFinanceData(resourceTypeEnum);
            return ResponseEntity.ok().body(res);
        } catch (BaseRestException e) {
            return e.toResponseEntity();
        }
    }
}
