package io.aditsukoco.allobank_test.controllers.finance;

import io.aditsukoco.allobank_test.models.enums.ResourceTypeEnum;
import org.springframework.http.ResponseEntity;


public interface FinanceControllerInterface {

    ResponseEntity<?> getFinanceData(ResourceTypeEnum resourceType);

}
