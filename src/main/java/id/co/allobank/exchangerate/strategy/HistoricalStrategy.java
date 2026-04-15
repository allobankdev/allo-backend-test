package id.co.allobank.exchangerate.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import id.co.allobank.exchangerate.client.FrankfurterClient;
import id.co.allobank.exchangerate.dto.BaseResponseDTO;

@Component
public class HistoricalStrategy implements FinanceDataStrategy {

    private final FrankfurterClient client;

    public HistoricalStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public BaseResponseDTO<?> fetch() {
        Map result = client.getHistoricalRates();
        return BaseResponseDTO.success(result);
    }
}