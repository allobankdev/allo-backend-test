package achlaq.co.allo_backend_test.finance.strategy;

import achlaq.co.allo_backend_test.common.util.SpreadFactorCalculator;
import achlaq.co.allo_backend_test.config.FrankfurterProperties;
import achlaq.co.allo_backend_test.external.frankfurter.FrankfurterClient;
import achlaq.co.allo_backend_test.external.frankfurter.dto.LatestRatesResponse;
import achlaq.co.allo_backend_test.finance.model.LatestIdrRatesView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IdrDataFetcher {

    private final FrankfurterClient client;
    private final FrankfurterProperties props;

    private volatile LatestIdrRatesView cached;

    @Override
    public void load() {
        LatestRatesResponse response = client.getLatestIdrRates();
        BigDecimal usdRate = response.getRates().get("USD");

        BigDecimal spreadFactor =
                SpreadFactorCalculator.calculateSpreadFactor(props.getGithubUsername());
        BigDecimal usdBuySpreadIdr =
                SpreadFactorCalculator.calculateUsdBuySpreadIdr(usdRate, spreadFactor);

        LatestIdrRatesView view = new LatestIdrRatesView(
                response.getDate(),
                response.getBase(),
                response.getRates(),
                spreadFactor,
                usdBuySpreadIdr
        );
        this.cached = view;
    }

    @Override
    public Object getCachedData() {
        return cached;
    }
}

