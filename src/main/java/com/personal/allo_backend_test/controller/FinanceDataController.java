package com.personal.allo_backend_test.controller;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.allo_backend_test.constant.ResponseConstant;
import com.personal.allo_backend_test.dto.Response;
import com.personal.allo_backend_test.strategy.IDRDataFetcher;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/finance/data")
public class FinanceDataController {

  private final Map<String, IDRDataFetcher> idrDataFetcherStrategies;

  @GetMapping("/{resourceType}")
  public Mono<ResponseEntity<Response<Object>>> fetchData(@PathVariable String resourceType) {
    return Mono.fromSupplier(() -> idrDataFetcherStrategies.get(resourceType))
      .filter(Objects::nonNull)
      .flatMap(IDRDataFetcher::fetch)
      .map(ResponseEntity::ok)
      .defaultIfEmpty(ResponseEntity.badRequest().body(Response.builder()
        .status(ResponseConstant.STATUS_FAILED)
        .data(Collections.emptyList())
        .message(String.format(ResponseConstant.MESSAGE_RESOURCE_TYPE_NOT_SUPPORTED, resourceType))
        .build()))
      .subscribeOn(Schedulers.boundedElastic());
  }
}
