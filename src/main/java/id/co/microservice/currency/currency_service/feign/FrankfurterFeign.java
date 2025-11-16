package id.co.microservice.currency.currency_service.feign;

import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@FeignClient(
    name = "frankfurter-feign",
    url = "https://api.frankfurter.dev/v1"
)
public interface FrankfurterFeign {

    @GetMapping(value = "/latest", produces = "application/json")
    FrankfurterResponseDto getLatestRate(
            @RequestParam("base") String base);

    @GetMapping(value = "/2024-01-01..2024-01-05?from=IDR&to=USD", produces = "application/json")
    FrankfurterResponseDto getHistoricalRate();

    @GetMapping(value = "/currencies", produces = "application/json")
    HashMap<String, String> getCurrencies();

}
