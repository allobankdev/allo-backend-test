package com.personal.allo_backend_test.strategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.allo_backend_test.constant.ResourceTypeConstant;
import com.personal.allo_backend_test.constant.ResponseConstant;
import com.personal.allo_backend_test.dto.FinanceDataDto;
import com.personal.allo_backend_test.dto.Response;
import com.personal.allo_backend_test.repository.InMemoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
  private final InMemoryRepository repository;

  @Override
  public String getResourceType() {
    return ResourceTypeConstant.SUPPORTED_CURRENCIES;
  }

  @Override
  public Mono<Response<Object>> fetch() {
    return repository.get(getResourceType())
      .map(data -> (Map<String, String>) data)
      .map(currencyMap -> Response.builder()
        .data(currencies(currencyMap))
        .resourceType(getResourceType())
        .status(ResponseConstant.STATUS_SUCCESS)
        .build());
  }

  private List<FinanceDataDto> currencies(Map<String, String> currencyMap) {
    return Optional.ofNullable(currencyMap)
      .map(Map::entrySet)
      .stream()
      .flatMap(Collection::stream)
      .map(entry -> new FinanceDataDto(
        entry.getKey(),
        entry.getValue(),
        null,
        null,
        null
      ))
      .toList();
  }
}
