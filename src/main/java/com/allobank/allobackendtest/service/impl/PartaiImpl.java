package com.allobank.allobackendtest.service.impl;

import com.allobank.allobackendtest.dto.PaginationDTO;
import com.allobank.allobackendtest.dto.PartaiFilterDTO;
import com.allobank.allobackendtest.dto.PartaiRequestDTO;
import com.allobank.allobackendtest.dto.PartaiResponseDTO;
import com.allobank.allobackendtest.exception.ValidatorService;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.service.PartaiService;
import com.allobank.allobackendtest.utility.CurrentTimestampUtility;
import com.allobank.allobackendtest.utility.PaginationUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class PartaiImpl implements PartaiService {
  @Autowired
  private ValidatorService validatorService;

  private final PartaiRepository partaiRepository;

  private PartaiResponseDTO toPartaiResponseFrom(Partai partai) {
    return PartaiResponseDTO.builder()
            .id(partai.getId())
            .namaPartai(partai.getNamaPartai())
            .nomorUrut(partai.getNomorUrut())
            .createdAt(partai.getCreatedAt())
            .updatedAt(partai.getUpdatedAt())
            .deletedAt(partai.getDeletedAt())
            .build();
  }

  @Override
  public List<PartaiResponseDTO> getListPartai(PartaiFilterDTO dto, PaginationDTO paginDTO) {
    var pagin = PaginationUtility.createPageable(
            paginDTO.getPage(),
            paginDTO.getLimit(),
            paginDTO.getSortBy(),
            paginDTO.getSortOrder()
        );

    var data = partaiRepository.findByFilter(dto.getNamaPartai(), dto.getNomorUrut(), pagin);

    return StreamSupport.stream(data.spliterator(), false)
            .map(this::toPartaiResponseFrom)
            .collect(Collectors.toList());
  }

  @Override
  public Integer getTotalPartai(PartaiFilterDTO dto) {
    return partaiRepository.countByFilter(dto.getNamaPartai(), dto.getNomorUrut());
  }

  @Override
  public PartaiResponseDTO getDetailPartaiById(UUID id) {
    var data = partaiRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "partai tidak ditemukan"));
    return toPartaiResponseFrom(data);
  }

  @Override
  @Transactional
  public void createPartai(PartaiRequestDTO req) {
    validatorService.validate(req);

    var timeNow = CurrentTimestampUtility.getLocalDateTime();
    var partai  = Partai.builder()
            .namaPartai(req.getNamaPartai())
            .nomorUrut(req.getNomorUrut())
            .createdAt(timeNow)
            .updatedAt(timeNow)
            .build();

    partaiRepository.save(partai);
  }

  @Override
  @Transactional
  public void updatePartai(UUID id, PartaiRequestDTO req) {
    validatorService.validate(req);

    var partai = partaiRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "partai tidak ditemukan"));

    partai.setNamaPartai(
      req.getNamaPartai() != null ? req.getNamaPartai() : partai.getNamaPartai()
    );

    partai.setNomorUrut(
      req.getNomorUrut() != null ? req.getNomorUrut() : partai.getNomorUrut()
    );

    partai.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    partaiRepository.save(partai);
  }

  @Override
  @Transactional
  public void activatePartai(UUID id) {
    var partai = partaiRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "partai tidak ditemukan"));

    partai.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    var deletedAt = partai.getDeletedAt();
    if(deletedAt != null) {
      partai.setDeletedAt(null);
    }

    partaiRepository.save(partai);
  }

  @Override
  @Transactional
  public void deactivatePartai(UUID id) {
    var partai = partaiRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "partai tidak ditemukan"));

    partai.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    var deletedAt = partai.getDeletedAt();
    if(deletedAt == null) {
      partai.setDeletedAt(CurrentTimestampUtility.getLocalDateTime());
    }

    partaiRepository.save(partai);
  }
}
