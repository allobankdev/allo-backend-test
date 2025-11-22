package com.personal.allo_backend_test.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.personal.allo_backend_test.client.FrankfurterClient;
import com.personal.allo_backend_test.constant.ResourceTypeConstant;
import com.personal.allo_backend_test.repository.InMemoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class InitialDataRunner implements ApplicationRunner {

  private final FrankfurterClient frankfurterClient;

  private final InMemoryRepository inMemoryRepository;

  @Override
  public void run(ApplicationArguments args) {
    Mono.when(frankfurterClient.fetchLatestRates()
          .flatMap(response -> inMemoryRepository.store(ResourceTypeConstant.LATEST_IDR_RATES, response)),
      frankfurterClient.fetchHistoricalRates()
        .flatMap(response -> inMemoryRepository.store(ResourceTypeConstant.HISTORICAL_IDR_USD, response)),
      frankfurterClient.fetchCurrencies()
        .flatMap(response -> inMemoryRepository.store(ResourceTypeConstant.SUPPORTED_CURRENCIES, response)))
      .then(inMemoryRepository.lock())
      .subscribeOn(Schedulers.boundedElastic())
      .subscribe();
  }
}
