package com.personal.allo_backend_test.strategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.allo_backend_test.client.response.HistoricalRatesResponse;
import com.personal.allo_backend_test.constant.ResourceTypeConstant;
import com.personal.allo_backend_test.constant.ResponseConstant;
import com.personal.allo_backend_test.dto.FinanceDataDto;
import com.personal.allo_backend_test.dto.Response;
import com.personal.allo_backend_test.properties.FrankfurterClientProperties;
import com.personal.allo_backend_test.repository.InMemoryRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HistoricalRatesStrategy implements IDRDataFetcher {
  private final InMemoryRepository repository;

  private final FrankfurterClientProperties frankfurterClientProperties;

  @Override
  public String getResourceType() {
    return ResourceTypeConstant.HISTORICAL_IDR_USD;
  }

  @Override
  public Mono<Response<Object>> fetch() {
    return repository.get(getResourceType())
      .map(data -> (HistoricalRatesResponse) data)
      .map(data -> Response.builder()
        .data(getDto(data))
        .resourceType(getResourceType())
        .status(ResponseConstant.STATUS_SUCCESS)
        .build());
  }

  private List<FinanceDataDto> getDto(HistoricalRatesResponse response) {
    return Optional.ofNullable(response)
      .filter(data -> StringUtils.isNotEmpty(data.base()))
      .map(HistoricalRatesResponse::rates)
      .map(Map::entrySet)
      .stream()
      .flatMap(Collection::stream)
      .map(entry ->
        new FinanceDataDto(
          null,
          null,
          entry.getValue().get(frankfurterClientProperties.getRate().getTo()),
          entry.getKey(),
          null))
      .toList();
  }
}
