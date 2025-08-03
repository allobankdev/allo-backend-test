package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.Request.PartaiRequestDTO;
import com.allobank.allobackendtest.dto.Response.PartaiResponseDTO;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.repository.PartaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartaiService {

    private final PartaiRepository partaiRepository;

    public PartaiResponseDTO create(PartaiRequestDTO request) {
        PartaiEntity entity = new PartaiEntity();
        entity.setId(UUID.randomUUID());
        entity.setNamaPartai(request.getNamaPartai());
        entity.setNomorUrut(request.getNomorUrut());

        PartaiEntity saved = partaiRepository.save(entity);

        PartaiResponseDTO response = new PartaiResponseDTO();
        response.setId(saved.getId());
        response.setNamaPartai(saved.getNamaPartai());
        response.setNomorUrut(saved.getNomorUrut());

        return response;
    }
}