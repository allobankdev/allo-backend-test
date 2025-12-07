package id.tisnanda.allobank.allo_bank_backend_test.strategy.impl;

import id.tisnanda.allobank.allo_bank_backend_test.constant.Constant;
import id.tisnanda.allobank.allo_bank_backend_test.dto.strategy.CurrenciesResponseDTO;
import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher<CurrenciesResponseDTO> {

    @Autowired
    public RestTemplate restTemplate;

    @Value("${external.frankfurter.currencies-url}")
    public String currenciesUrl;

    @Override
    public List<CurrenciesResponseDTO> fetchData() {

        Map<String, String> response = restTemplate.getForObject(currenciesUrl, Map.class);

        if (response == null) {
            throw new BadRequestException(Constant.FAILED_FETCH_SUPPORTED_CURRENCIES);
        }

        CurrenciesResponseDTO dto = new CurrenciesResponseDTO(response);

        return Collections.singletonList(dto);
    }
}
