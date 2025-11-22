package com.personal.allo_backend_test.strategy;

import com.personal.allo_backend_test.dto.Response;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
  Mono<Response<Object>> fetch();
  String getResourceType();
}

