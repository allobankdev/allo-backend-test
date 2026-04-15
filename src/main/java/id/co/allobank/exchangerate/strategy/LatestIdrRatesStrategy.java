package id.co.allobank.exchangerate.strategy;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import id.co.allobank.exchangerate.client.FrankfurterClient;
import id.co.allobank.exchangerate.common.Constant;
import id.co.allobank.exchangerate.dto.BaseResponseDTO;
import id.co.allobank.exchangerate.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LatestIdrRatesStrategy implements FinanceDataStrategy {

    private final FrankfurterClient client;

    public LatestIdrRatesStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public BaseResponseDTO<?> fetch() {

        log.info("CALLING API: latest_idr_rates");

        Map response = client.getLatestRates();

        log.info("RESPONSE: " + response);

        if (response == null) {
        log.error("API response is null for latest_idr_rates");
        throw new CustomException(
                "API latest_idr_rates returned null",
                HttpStatus.INTERNAL_SERVER_ERROR,
                Constant.EXTERNAL_SERVICE_ERROR
        );
    }

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        if (rates == null) {
            log.error("Rates data is null in API response");
            throw new CustomException(
                    "Rates is null from API",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    Constant.EXTERNAL_SERVICE_ERROR
            );
        }

        if (!rates.containsKey("USD") || rates.get("USD") == null) {
            log.error("USD rate is missing or null in API response");
            throw new CustomException(
                    "USD rate is missing or null from API response",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    Constant.EXTERNAL_SERVICE_ERROR
            );
        }

        double usdRate = rates.get("USD");

        if (usdRate <= 0) {
            log.error("Invalid USD rate received: {}", usdRate);
            throw new CustomException(
                "Invalid USD rate received from API: " + usdRate,
                HttpStatus.INTERNAL_SERVER_ERROR,
                Constant.EXTERNAL_SERVICE_ERROR
            );
        }
        
        double spread = calculateSpreadFactor(Constant.GITHUB_USERNAME);

        double result = (1 / usdRate) * (1 + spread);

        rates.put("USD_BuySpread_IDR", result);

        return BaseResponseDTO.success(response);
    }

    private double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
