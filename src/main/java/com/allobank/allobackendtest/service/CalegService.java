package com.allobank.allobackendtest.service;


import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;

    public List<Caleg> getAllCaleg(String dapilId, String partaiId, String sortBy) {
        Specification<Caleg> spec = (root, query, criteriaBuilder) -> {
            // Filter Dapil
            if (dapilId != null) {
                return criteriaBuilder.equal(root.get("dapil").get("id"), UUID.fromString(dapilId));
            }
            // Filter Partai
            if (partaiId != null) {
                return criteriaBuilder.equal(root.get("partai").get("id"), UUID.fromString(partaiId));
            }
            return criteriaBuilder.conjunction(); // return all if no filters
        };

        return calegRepository.findAll(Sort.by(sortBy));
    }
}
