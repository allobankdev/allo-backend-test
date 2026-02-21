package io.aditsukoco.allobank_test.controllers.finance;

import org.springframework.http.ResponseEntity;


public interface FinanceControllerInterface {

    public ResponseEntity<?> getFinanceData(String resourceType);

}
