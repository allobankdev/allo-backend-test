package com.allobankdev.allo_idr_rate_aggregator.service;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceDataStore {

    // private volatile Map<String, List<Object>> store =
    //         Collections.emptyMap();

    // public void initialize(Map<String, List<Object>> data) {
    //     this.store = Collections.unmodifiableMap(data);
    // }

    // public Mono<List<Object>> get(String resourceType) {
    //     return Mono.justOrEmpty(store.get(resourceType))
    //             .defaultIfEmpty(Collections.emptyList());
    // }
    private final ReactiveMongoTemplate mongoTemplate;

    public void initialize(Map<String, List<Object>> data) {
        data.forEach((resourceType, list) -> {
            // Simpan ke collection sesuai namanya (misal: latest_idr_rates)
            mongoTemplate.remove(new org.springframework.data.mongodb.core.query.Query(), resourceType)
                .thenMany(Flux.fromIterable(list))
                .flatMap(item -> mongoTemplate.save(item, resourceType))
                .subscribe(
                    saved -> System.out.println("Berhasil simpan ke MongoDB collection: " + resourceType),
                    err -> System.err.println("Gagal simpan " + resourceType + ": " + err.getMessage())
                );
        });
    }

    public Mono<List<Object>> get(String resourceType) {
        // Ambil data dari MongoDB, bukan dari Map memory
        return mongoTemplate.findAll(Object.class, resourceType)
                .collectList()
                .defaultIfEmpty(Collections.emptyList());
    }
}
