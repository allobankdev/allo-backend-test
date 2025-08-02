package com.allobank.allobackendtest.service.impl;

import com.allobank.allobackendtest.dto.CalegDTO;
import com.allobank.allobackendtest.dto.CalegFilterDTO;
import com.allobank.allobackendtest.mapper.CalegMapper;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.service.CalegService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CalegServiceImpl implements CalegService {

    private final CalegRepository calegRepository;
    private final CalegMapper calegMapper;

    @Override
    public Page<CalegDTO> findAllWithFilter(CalegFilterDTO filter, Pageable pageable) {
        log.debug("Finding Caleg with filter: {}", filter);

        Page<Caleg> calegPage = calegRepository.findAllWithFilter(
                filter.getDapilId(),
                filter.getPartaiId(),
                pageable
        );

        return calegPage.map(calegMapper::toDTO);
    }

    @Override
    public Optional<CalegDTO> findById(UUID id) {
        log.debug("Finding Caleg by id: {}", id);
        return calegRepository.findById(id)
                .map(calegMapper::toDTO);
    }
}