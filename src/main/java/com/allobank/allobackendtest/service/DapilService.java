package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.dto.DapilDto;
import com.allobank.allobackendtest.exception.EntityNotFoundException;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;

@Service
public class DapilService {

  @Autowired
  private DapilRepository dapilRepository;

  public List<Dapil> getAllDapil() {
    return dapilRepository.findAll();
  }

  public Dapil createDapil(Dapil dapil) {
    return dapilRepository.save(dapil);
  }

  public Dapil getDapilById(UUID id) {
    return dapilRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Dapil not found"));
  }

  public DapilDto convertToDto(Dapil dapil) {
    DapilDto dapilDto = new DapilDto();
    dapilDto.setId(dapil.getId());
    dapilDto.setNama_dapil(dapil.getNama_dapil());
    return dapilDto;
  }

  public Dapil convertToEntity(DapilDto dapilDto) {
    Dapil dapil = new Dapil();
    dapil.setId(dapilDto.getId());
    dapil.setNama_dapil(dapilDto.getNama_dapil());
    return dapil;
  }
}
