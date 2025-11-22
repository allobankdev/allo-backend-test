package com.personal.allo_backend_test.repository;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class InMemoryRepository {

  private final AtomicReference<Map<String, Object>> dataStore = new AtomicReference<>(new ConcurrentHashMap<>());

  public <T> Mono<Void> store(String resourceType, T data) {
    return Mono.fromRunnable(() -> {
      dataStore.get().put(resourceType, data);
    });
  }

  public Mono<Void> lock() {
    return Mono.fromRunnable(() -> dataStore.set(Collections.unmodifiableMap(dataStore.get())));
  }

  public Mono<Object> get(String resourceType) {
    return Mono.fromSupplier(() -> dataStore.get().get(resourceType));
  }
}
