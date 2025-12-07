package id.tisnanda.allobank.allo_bank_backend_test.strategy.impl;

import id.tisnanda.allobank.allo_bank_backend_test.constant.Constant;
import id.tisnanda.allobank.allo_bank_backend_test.dto.strategy.LatestIDRRateResponseDTO;
import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.exception.handler.GlobalExceptionHandler;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.IDRDataFetcher;
import lombok.Getter;
import lombok.Setter;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("latest_idr_rates")
@Setter
@Getter
public class LatestIDRRatesFetcher implements IDRDataFetcher<LatestIDRRateResponseDTO> {

    private static final Logger log = Logger.getLogger(LatestIDRRatesFetcher.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${external.frankfurter.latest-url}")
    String latestUrl;

    @Value("${spread.github-username}")
    String githubUsername;

    @Override
    public List<LatestIDRRateResponseDTO> fetchData() {

        Map<String, Object> response = restTemplate.getForObject(latestUrl, Map.class);

        if (response == null || !response.containsKey(Constant.RATES_KEY)) {
            throw new BadRequestException(Constant.FAILED_FETCH_LATEST_IDR_RATES);
        }

        Map<String, Object> rates = (Map<String, Object>) response.get(Constant.RATES_KEY);
        double rateUSD = ((Number) rates.get(Constant.USD)).doubleValue();
        double spreadFactor = calculateSpreadFactor(githubUsername);
        double usdBuySpread = (1 / rateUSD) * (1 + spreadFactor);

        Map<String, Double> mappedRates = Map.of(Constant.IDR, usdBuySpread);

        LatestIDRRateResponseDTO dto = new LatestIDRRateResponseDTO(
                (String) response.getOrDefault(Constant.DATE, LocalDate.now().toString()),
                rateUSD,
                usdBuySpread
        );

        return Collections.singletonList(dto);
    }

    public double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }

}
