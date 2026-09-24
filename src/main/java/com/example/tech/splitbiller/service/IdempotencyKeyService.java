package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.IdempotencyKey;
import com.example.tech.splitbiller.repository.IdempotencyKeyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdempotencyKeyService {

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public IdempotencyKeyService(IdempotencyKeyRepository idempotencyKeyRepository) {
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    public Optional<IdempotencyKey> findByIdempotencyKey(String idempotencyKey) {
        return idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey);
    }

    public void save(String key, String hashRequest, String response) {
        IdempotencyKey idempotencyKey = new IdempotencyKey();
        idempotencyKey.setIdempotencyKey(key);
        idempotencyKey.setHashRequest(hashRequest);
        idempotencyKey.setResponse(response);
        idempotencyKey.setCreatedAt(System.currentTimeMillis());
        idempotencyKeyRepository.save(idempotencyKey);
    }
}
