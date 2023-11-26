package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface PartaiService {
    Partai createPartai(Partai partai);

    Partai getPartaiById(UUID partaiId);

    List<Partai> getAllPartai();

    List<Partai> getAllPartaiSort(Sort sort);

    Partai updatePartai(Partai partai);

    void deletePartai(UUID partaiId);
}
