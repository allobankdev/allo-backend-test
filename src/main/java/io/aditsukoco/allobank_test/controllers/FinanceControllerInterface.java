package io.aditsukoco.allobank_test.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


public interface FinanceControllerInterface {

    public ResponseEntity<?> getFinanceData(String resourceType);

}
