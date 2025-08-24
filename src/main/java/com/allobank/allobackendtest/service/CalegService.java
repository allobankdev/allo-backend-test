package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.CalegResponse;
import com.allobank.allobackendtest.dto.SearchCalegRequest;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CalegService {

    private final CalegRepository calegRepository;

    @Transactional(readOnly = true)
    public Page<CalegResponse> searchCaleg(SearchCalegRequest request){
        Specification<Caleg> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(Objects.nonNull(request.dapil())){
                Join<Caleg, Dapil> dapilJoin = root.join("dapil");
                predicates.add(criteriaBuilder.equal(dapilJoin.get("namaDapil"),request.dapil()));
            }

            if(Objects.nonNull(request.partai())){
                Join<Caleg, Partai> partaiJoin = root.join("partai");
                predicates.add(criteriaBuilder.equal(partaiJoin.get("namaPartai"),request.partai()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });

        Sort sort = Sort.by(Sort.Direction.ASC, request.sortBy());


        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
        Page<Caleg> calegList = calegRepository.findAll(specification, pageable);
        List<CalegResponse> calegResponses = calegList.getContent().stream()
                .map(caleg -> CalegResponse.builder()
                        .id(caleg.getId())
                        .nomorUrut(caleg.getNomorUrut())
                        .nama(caleg.getNama())
                        .jenisKelamin(caleg.getJenisKelamin())
                        .build())
                .toList();

        return new PageImpl<>(calegResponses, pageable, calegList.getTotalElements());
    }
}

