package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.util.dto.PartaiRequestDTO;
import com.allobank.allobackendtest.util.dto.PartaiResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PartaiService {
    List<Partai> getAllPartai();
    PartaiResponseDTO createPartai(PartaiRequestDTO partai);
    PartaiResponseDTO updatePartai(UUID id, PartaiRequestDTO partai);
    void deletePartai(UUID id);
}
