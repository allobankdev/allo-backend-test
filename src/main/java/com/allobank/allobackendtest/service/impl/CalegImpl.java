package com.allobank.allobackendtest.service.impl;

import com.allobank.allobackendtest.dto.PaginationDTO;
import com.allobank.allobackendtest.dto.CalegFilterDTO;
import com.allobank.allobackendtest.dto.CalegRequestDTO;
import com.allobank.allobackendtest.dto.CalegResponseDTO;
import com.allobank.allobackendtest.exception.ValidatorService;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.service.CalegService;
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
public class CalegImpl implements CalegService {
  @Autowired
  private ValidatorService validatorService;

  private final CalegRepository calegRepository;
  private final DapilRepository dapilRepository;
  private final PartaiRepository partaiRepository;

  private CalegResponseDTO toCalegResponseFrom(Caleg caleg) {
    return CalegResponseDTO.builder()
            .id(caleg.getId())
            .nomorUrut(caleg.getNomorUrut())
            .dapil(caleg.getDapil())
            .partai(caleg.getPartai())
            .nama(caleg.getNama())
            .jenisKelamin(caleg.getJenisKelamin())
            .createdAt(caleg.getCreatedAt())
            .updatedAt(caleg.getUpdatedAt())
            .deletedAt(caleg.getDeletedAt())
            .build();
  }

  @Override
  public List<CalegResponseDTO> getListCaleg(CalegFilterDTO dto, PaginationDTO paginDTO) {
    var pagin = PaginationUtility.createPageable(
            paginDTO.getPage(),
            paginDTO.getLimit(),
            paginDTO.getSortBy(),
            paginDTO.getSortOrder()
    );

    var dapilId = dto.getDapilId() == null ? "" : dto.getDapilId().toString();
    var partaiId = dto.getPartaiId() == null ? "" : dto.getPartaiId().toString();

    var data = calegRepository.findByFilter(
            dto.getNama(),
            dto.getNomorUrut(),
            dapilId,
            partaiId,
            pagin
    );

    return StreamSupport.stream(data.spliterator(), false)
            .map(this::toCalegResponseFrom)
            .collect(Collectors.toList());
  }

  @Override
  public Integer getTotalCaleg(CalegFilterDTO dto) {
    var dapilId = dto.getDapilId() == null ? "" : dto.getDapilId().toString();
    var partaiId = dto.getPartaiId() == null ? "" : dto.getPartaiId().toString();

    return calegRepository.countByFilter(
            dto.getNama(),
            dto.getNomorUrut(),
            dapilId,
            partaiId
    );
  }

  @Override
  public CalegResponseDTO getDetailCalegById(UUID id) {
    var data = calegRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "caleg tidak ditemukan"));
    return toCalegResponseFrom(data);
  }

  @Override
  @Transactional
  public void createCaleg(CalegRequestDTO req) {
    validatorService.validate(req);

    var dapil = dapilRepository.findById(req.getDapilId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dapil not found"));
    var partai = partaiRepository.findById(req.getPartaiId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "partai not found"));
    var timeNow = CurrentTimestampUtility.getLocalDateTime();

    var caleg  = Caleg.builder()
            .nama(req.getNama())
            .dapil(dapil)
            .partai(partai)
            .nomorUrut(req.getNomorUrut())
            .jenisKelamin(req.getJenisKelamin())
            .createdAt(timeNow)
            .updatedAt(timeNow)
            .build();

    calegRepository.save(caleg);
  }

  @Override
  @Transactional
  public void updateCaleg(UUID id, CalegRequestDTO req) {
    validatorService.validate(req);

    var caleg = calegRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "caleg tidak ditemukan"));

    var dapil = dapilRepository.findById(req.getDapilId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dapil not found"));
    var partai = partaiRepository.findById(req.getPartaiId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "partai not found"));

    caleg.setNama(
            req.getNama() != null ? req.getNama() : caleg.getNama()
    );

    caleg.setDapil(
            req.getDapilId() != null ? dapil : caleg.getDapil()
    );

    caleg.setPartai(
            req.getDapilId() != null ? partai : caleg.getPartai()
    );

    caleg.setNomorUrut(
            req.getNomorUrut() != 0 ? req.getNomorUrut() : caleg.getNomorUrut()
    );

    caleg.setJenisKelamin(
            req.getJenisKelamin() !=  null ? req.getJenisKelamin() : caleg.getJenisKelamin()
    );

    caleg.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    calegRepository.save(caleg);
  }

  @Override
  @Transactional
  public void activateCaleg(UUID id) {
    var caleg = calegRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "caleg tidak ditemukan"));

    caleg.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    var deletedAt = caleg.getDeletedAt();
    if(deletedAt != null) {
      caleg.setDeletedAt(null);
    }

    calegRepository.save(caleg);
  }

  @Override
  @Transactional
  public void deactivateCaleg(UUID id) {
    var caleg = calegRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "caleg tidak ditemukan"));

    caleg.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    var deletedAt = caleg.getDeletedAt();
    if(deletedAt == null) {
      caleg.setDeletedAt(CurrentTimestampUtility.getLocalDateTime());
    }

    calegRepository.save(caleg);
  }
}
