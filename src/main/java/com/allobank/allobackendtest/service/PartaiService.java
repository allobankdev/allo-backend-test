package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.dto.PartaiDto;
import com.allobank.allobackendtest.exception.EntityNotFoundException;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;

@Service
public class PartaiService {
  @Autowired
  private PartaiRepository partaiRepository;

  public List<Partai> getAllPartai() {
    return partaiRepository.findAll();
  }

  public Partai createPartai(Partai partai) {
    return partaiRepository.save(partai);
  }

  public Partai getPartaiById(UUID id) {
    return partaiRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Partai not found"));
  }

  public PartaiDto convertToDto(Partai partai) {
    PartaiDto partaiDto = new PartaiDto();
    partaiDto.setId(partai.getId());
    partaiDto.setNama_partai(partai.getNama_partai());
    return partaiDto;
  }

  public Partai convertToEntity(PartaiDto partaiDto) {
    Partai partai = new Partai();
    partai.setId(partaiDto.getId());
    partai.setNama_partai(partaiDto.getNama_partai());
    return partai;
  }
}
