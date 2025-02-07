package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.exception.EntityNotFoundException;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CalegService {
  @Autowired
  private CalegRepository calegRepository;

  @Autowired
  private DapilRepository dapilRepository;

  @Autowired
  private PartaiRepository partaiRepository;

  public List<Caleg> getAllCaleg() {
    return calegRepository.findAll();
  }

  public Page<Caleg> getAllCaleg(Pageable pageable) {
    return calegRepository.findAll(pageable);
  }

  public Caleg createCaleg(Caleg caleg) {
    UUID dapilId = caleg.getDapil().getId();
    UUID partaiId = caleg.getPartai().getId();

    Dapil dapil = dapilRepository.findById(dapilId)
        .orElseThrow(() -> new EntityNotFoundException("Dapil not found with id: " + dapilId));
    Partai partai = partaiRepository.findById(partaiId)
        .orElseThrow(() -> new EntityNotFoundException("Partai not found with id: " + partaiId));

    caleg.setDapil(dapil);
    caleg.setPartai(partai);

    return calegRepository.save(caleg);
  }

  public List<Caleg> getCalegByDapilAndPartai(UUID dapilId, UUID partaiId) {
    return calegRepository.findByDapilIdAndPartaiId(dapilId, partaiId);
  }

  public List<Caleg> getCalegSortedByNomorUrut(Sort.Direction direction) {
    if (direction == Sort.Direction.ASC) {
      return calegRepository.findAllByOrderByNomorUrutAsc();
    } else {
      return calegRepository.findAllByOrderByNomorUrutDesc();
    }
  }

  public CalegDto convertToDto(Caleg caleg) {
    CalegDto calegDto = new CalegDto();

    calegDto.setId(caleg.getId().toString());
    calegDto.setNama(caleg.getNama());
    calegDto.setNomor_urut(caleg.getNomor_urut());

    if (caleg.getJenisKelamin() != null) {
      calegDto.setJenisKelamin(caleg.getJenisKelamin().name());
    }

    calegDto.setAlamat(caleg.getAlamat());

    if (caleg.getDapil() != null) {
      calegDto.setDapilId(caleg.getDapil().getId().toString());
    }

    if (caleg.getPartai() != null) {
      calegDto.setPartaiId(caleg.getPartai().getId().toString());
    }

    return calegDto;
  }

  public Caleg convertToEntity(CalegDto calegDto) {
    Caleg caleg = new Caleg();

    if (calegDto.getId() != null) {
      caleg.setId(UUID.fromString(calegDto.getId()));
    }

    caleg.setNama(calegDto.getNama());
    caleg.setNomor_urut(calegDto.getNomor_urut());

    if (calegDto.getJenisKelamin() != null && !calegDto.getJenisKelamin().isEmpty()) {
      try {
        caleg.setJenisKelamin(JenisKelamin.valueOf(calegDto.getJenisKelamin()));
      } catch (IllegalArgumentException ex) {
        log.error("Invalid JenisKelamin: {}", calegDto.getJenisKelamin());
      }
    }

    caleg.setAlamat(calegDto.getAlamat());

    if (calegDto.getDapilId() != null) { // Perbaikan: getDapilId
      Dapil dapil = dapilRepository.findById(UUID.fromString(calegDto.getDapilId()))
          .orElseThrow(() -> new EntityNotFoundException("Dapil not found"));
      caleg.setDapil(dapil);
    }

    if (calegDto.getPartaiId() != null) { // Perbaikan: getPartaiId
      Partai partai = partaiRepository.findById(UUID.fromString(calegDto.getPartaiId()))
          .orElseThrow(() -> new EntityNotFoundException("Partai not found"));
      caleg.setPartai(partai);
    }

    return caleg;
  }
}
