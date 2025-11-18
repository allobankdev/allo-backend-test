package com.allo.test.modules.finance.client;

import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.dto.res.LatestRatesResponse;
import com.allo.test.modules.finance.client.strategy.impl.CurrenciesStrategy;
import com.allo.test.modules.finance.client.strategy.impl.HistoricalRatesStrategy;
import com.allo.test.modules.finance.client.strategy.impl.LatestRatesStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrankfurterClient {

    private final WebClient webClient;
    private final LatestRatesStrategy latestRatesStrategy;
    private final HistoricalRatesStrategy historicalRatesStrategy;
    private final CurrenciesStrategy currenciesStrategy;

    /**
     * Fetches the latest exchange rates using the LatestRatesStrategy.
     *
     * @return LatestRatesResponse containing current exchange rates
     */
    public LatestRatesResponse fetchLatestRates() {
        log.info("Executing {} to fetch latest rates", latestRatesStrategy.getStrategyName());
        return latestRatesStrategy.fetchData(webClient);
    }

    /**
     * Fetches historical exchange rates using the HistoricalRatesStrategy.
     *
     * @return HistoricalRatesResponse containing historical exchange rates
     */
    public HistoricalRatesResponse fetchHistoricalRates() {
        log.info("Executing {} to fetch historical rates", historicalRatesStrategy.getStrategyName());
        return historicalRatesStrategy.fetchData(webClient);
    }

    /**
     * Fetches the list of supported currencies using the CurrenciesStrategy.
     *
     * @return CurrenciesResponse containing all supported currencies
     */
    public CurrenciesResponse fetchCurrencies() {
        log.info("Executing {} to fetch currencies", currenciesStrategy.getStrategyName());
        return currenciesStrategy.fetchData(webClient);
    }
}
