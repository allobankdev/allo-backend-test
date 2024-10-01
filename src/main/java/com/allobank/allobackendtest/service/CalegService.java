package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;

    public List<Caleg> getAllCalegs(UUID dapilId, UUID partaiId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("nomorUrut"));

        if (dapilId != null && partaiId != null) {
            return calegRepository.findByDapilIdAndPartaiId(dapilId, partaiId, pageRequest);
        } else if (dapilId != null) {
            return calegRepository.findByDapilId(dapilId, pageRequest);
        } else if (partaiId != null) {
            return calegRepository.findByPartaiId(partaiId, pageRequest);
        } else {
            return calegRepository.findAll(pageRequest).getContent();
        }
    }
}
