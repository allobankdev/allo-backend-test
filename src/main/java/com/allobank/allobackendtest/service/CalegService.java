package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.CalegDTO;
import com.allobank.allobackendtest.dto.CalegFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CalegService {
    Page<CalegDTO> findAllWithFilter(CalegFilterDTO filter, Pageable pageable);
    Optional<CalegDTO> findById(UUID id);
}