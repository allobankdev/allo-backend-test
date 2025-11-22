package com.personal.allo_backend_test.strategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.allo_backend_test.client.response.LatestRatesResponse;
import com.personal.allo_backend_test.constant.ResourceTypeConstant;
import com.personal.allo_backend_test.constant.ResponseConstant;
import com.personal.allo_backend_test.dto.FinanceDataDto;
import com.personal.allo_backend_test.dto.Response;
import com.personal.allo_backend_test.properties.FrankfurterClientProperties;
import com.personal.allo_backend_test.properties.GithubProperties;
import com.personal.allo_backend_test.repository.InMemoryRepository;
import com.personal.allo_backend_test.util.SpreadUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LatestIdrRatesStrategy implements IDRDataFetcher {

  private static final String IDR = "IDR";
  private static final String USD = "USD";

  private final InMemoryRepository repository;

  private final GithubProperties githubProperties;

  private final FrankfurterClientProperties frankfurterClientProperties;

  @Override
  public String getResourceType() {
    return ResourceTypeConstant.LATEST_IDR_RATES;
  }

  @Override
  public Mono<Response<Object>> fetch() {
    return repository.get(getResourceType())
      .map(data -> (LatestRatesResponse) data)
      .map(data -> Response.builder()
        .data(getDto(data))
        .resourceType(getResourceType())
        .status(ResponseConstant.STATUS_SUCCESS)
        .build());
  }

  private List<FinanceDataDto> getDto(LatestRatesResponse response) {
    return Optional.ofNullable(response)
      .map(LatestRatesResponse::rates)
      .map(Map::entrySet)
      .stream()
      .flatMap(Collection::stream)
      .map(entry -> {
        String base = frankfurterClientProperties.getRate().getFrom();
        String key = entry.getKey();
        Double rate = entry.getValue();

        Double usdBuySpreadIdr = null;
        if (USD.equals(key) && IDR.equals(base)) {
          usdBuySpreadIdr = SpreadUtil.buySpread(githubProperties.getUsername(), rate);
        }
        return new FinanceDataDto(
          key,
          null,
          rate,
          null,
          usdBuySpreadIdr
        );
      })
      .toList();
  }
}
