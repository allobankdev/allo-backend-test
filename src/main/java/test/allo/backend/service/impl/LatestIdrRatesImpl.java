package test.allo.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import test.allo.backend.service.IDRDataFetcher;

@Slf4j
@Service("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIdrRatesImpl implements IDRDataFetcher {

    @Value("${internal.data.github.username}")
    String username;

    @Value("${external.frankfurter.endpoint.latest-idr-rates}")
    String uriLatestIdrRate;

    private final ObjectMapper mapper;
    private final WebClient webClient;

    @Override
    public JsonNode fetchData() {

        int unicodeSum = 0;
        double rateUsd = 0d;

        log.info("username value: {}", username);
        for(char c : username.toLowerCase().toCharArray()) unicodeSum += c;

        double spreadFactor = (unicodeSum % 1000) / 100000.0;
        log.info("spreadFactor value: {}", spreadFactor);

        JsonNode externalResponse = webClient.get()
                .uri(uriLatestIdrRate)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        log.info("external API response: {}", externalResponse);

        if(externalResponse != null && externalResponse.has("rates")) {
            rateUsd = externalResponse
                    .path("rates")
                    .path("USD")
                    .asDouble(0.0);
        }
        log.info("rateUsd value: {}", rateUsd);

        double usbBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
        log.info("usbBuySpreadIdr value: {}", usbBuySpreadIdr);

        ObjectNode response = mapper.createObjectNode();
        response.put("username", username.toLowerCase());
        response.put("unicode", unicodeSum);
        response.put("spreadFactor", spreadFactor);
        response.put("rateUsd", rateUsd);
        response.put("usdBuySpreadIdr", usbBuySpreadIdr);

        return response;
    }
}
