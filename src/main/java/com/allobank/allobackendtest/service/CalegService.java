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
    if (caleg.getDapil() != null && caleg.getDapil().getId() != null) {
      dapilRepository.findById(caleg.getDapil().getId())
          .orElseThrow(() -> new EntityNotFoundException("Dapil not found"));
    }
    if (caleg.getPartai() != null && caleg.getPartai().getId() != null) {
      partaiRepository.findById(caleg.getPartai().getId())
          .orElseThrow(() -> new EntityNotFoundException("Partai not found"));
    }
    return calegRepository.save(caleg);
  }

  public List<Caleg> getCalegByDapilAndPartai(UUID dapilId, UUID partaiId) {
    return calegRepository.findByDapilAndPartai(dapilId, partaiId);
  }

  public List<Caleg> getCalegSortedByNomorUrut(Sort.Direction direction) {
    return calegRepository.findAll(Sort.by(direction, "nomor_urut"));
  }

  public CalegDto convertToDto(Caleg caleg) {
    CalegDto calegDto = new CalegDto();
    calegDto.setId(caleg.getId());
    calegDto.setNama(caleg.getNama());
    calegDto.setNomor_urut(caleg.getNomor_urut());
    if (caleg.getJenisKelamin() != null) {
      calegDto.setJenisKelamin(caleg.getJenisKelamin().name());
    }
    calegDto.setAlamat(caleg.getAlamat());
    if (caleg.getDapil() != null) {
      calegDto.setDapilId(caleg.getDapil().getId());
    }
    if (caleg.getPartai() != null) {
      calegDto.setPartaiId(caleg.getPartai().getId());
    }
    return calegDto;
  }

  public Caleg convertToEntity(CalegDto calegDto) {
    Caleg caleg = new Caleg();
    caleg.setId(calegDto.getId());
    caleg.setNama(calegDto.getNama());
    caleg.setNomor_urut(calegDto.getNomor_urut());
    if (calegDto.getJenisKelamin() != null) {
      caleg.setJenisKelamin(JenisKelamin.valueOf(calegDto.getJenisKelamin()));
    }
    caleg.setAlamat(calegDto.getAlamat());

    if (calegDto.getDapilId() != null) {
      Dapil dapil = dapilRepository.findById(calegDto.getDapilId())
          .orElseThrow(() -> new EntityNotFoundException("Dapil not found"));
      caleg.setDapil(dapil);
    }

    if (calegDto.getPartaiId() != null) {
      Partai partai = partaiRepository.findById(calegDto.getPartaiId())
          .orElseThrow(() -> new EntityNotFoundException("Partai not found"));
      caleg.setPartai(partai);
    }
    return caleg;
  }
}
