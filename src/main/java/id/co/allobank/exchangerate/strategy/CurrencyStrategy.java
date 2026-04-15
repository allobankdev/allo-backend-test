package id.co.allobank.exchangerate.strategy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.co.allobank.exchangerate.client.FrankfurterClient;
import id.co.allobank.exchangerate.dto.BaseResponseDTO;

@Component
public class CurrencyStrategy implements FinanceDataStrategy {

    @Autowired
    private FrankfurterClient client;

    // public CurrencyStrategy(FrankfurterClient client) {
    //     this.client = client;
    // }

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public BaseResponseDTO<?> fetch() {
        Map<String, String> result = client.getCurrencies();
        return BaseResponseDTO.success(result);
    }
}