package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;

    public Page<Caleg> getAllCaleg(String namaDapil, String namaPartai, String sortBy, Integer pageNumber, Integer limit) {
        log.info("start get all data caleg");
        try {
            Specification<Caleg> spec = (root, query, criteriaBuilder) -> {
                if (namaDapil != null) {
                    return criteriaBuilder.equal(root.get("dapil").get("namaDapil"),(namaDapil));
                }
                if (namaPartai != null) {
                    return criteriaBuilder.equal(root.get("partai").get("namaPartai"),(namaPartai));
                }
                return criteriaBuilder.conjunction();
            };

            Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by(sortBy).descending());
            log.info("and get all data caleg");
            return calegRepository.findAll(spec, pageable);
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
            return null;
        }
    }
}
