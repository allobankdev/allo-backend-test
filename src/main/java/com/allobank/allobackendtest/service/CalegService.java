package com.allobank.allobackendtest.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalegService implements CalegServiceInterface {

  private final CalegRepository calegRepository;

  @Override
  public List<Caleg> getCalegListByDapilAndPartai(String namaDapil, String namaPartai) {
    Specification<Caleg> spec = Specification.where((root, query, criteriaBuilder) -> {
      return criteriaBuilder.and(
        namaDapil == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("dapil").get("namaDapil"), namaDapil),
        namaPartai == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("partai").get("namaPartai"), namaPartai)
        );
    });
    return calegRepository.findAll(spec);
  }

}
