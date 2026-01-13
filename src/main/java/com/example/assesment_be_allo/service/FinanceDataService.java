package com.example.assesment_be_allo.service;
import com.example.assesment_be_allo.exception.InvalidResourceTypeException;
import org.springframework.stereotype.Service;

@Service
public class FinanceDataService {

    private final LatestIdrRatesService latestIdrRatesService;
    private final HistoricalIdrUsdService historicalIdrUsdService;
    private final SupportedCurrenciesService supportedCurrenciesService;

    public FinanceDataService(LatestIdrRatesService latestIdrRatesService,
                              HistoricalIdrUsdService historicalIdrUsdService,
                              SupportedCurrenciesService supportedCurrenciesService) {
        this.latestIdrRatesService = latestIdrRatesService;
        this.historicalIdrUsdService = historicalIdrUsdService;
        this.supportedCurrenciesService = supportedCurrenciesService;
    }

    public Object fetchData(String resourceType) {
        switch (resourceType) {
            case "latest_idr_rates":
                return latestIdrRatesService.fetchLatestRates();
            case "historical_idr_usd":
                return historicalIdrUsdService.fetchHistoricalRates();
            case "supported_currencies":
                return supportedCurrenciesService.fetchSupportedCurrencies();
            default:
                throw new InvalidResourceTypeException(
                        "Unknown resource type: " + resourceType +
                                ". Supported types are: latest_idr_rates, historical_idr_usd, supported_currencies");
        }
    }
}