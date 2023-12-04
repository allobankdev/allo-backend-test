package com.allobank.allobackendtest.service.impl;

import com.allobank.allobackendtest.dto.PaginationDTO;
import com.allobank.allobackendtest.dto.DapilFilterDTO;
import com.allobank.allobackendtest.dto.DapilRequestDTO;
import com.allobank.allobackendtest.dto.DapilResponseDTO;
import com.allobank.allobackendtest.exception.ValidatorService;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.service.DapilService;
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
public class DapilImpl implements DapilService {
  private ValidatorService validatorService;

  private final DapilRepository dapilRepository;

  private DapilResponseDTO toDapilResponseFrom(Dapil dapil) {
    return DapilResponseDTO.builder()
            .id(dapil.getId())
            .namaDapil(dapil.getNamaDapil())
            .provinsi(dapil.getProvinsi())
            .jumlahKursi(dapil.getJumlahKursi())
            .wilayahDapilList(dapil.getWilayahDapilList())
            .createdAt(dapil.getCreatedAt())
            .updatedAt(dapil.getUpdatedAt())
            .deletedAt(dapil.getDeletedAt())
            .build();
  }

  @Override
  public List<DapilResponseDTO> getListDapil(DapilFilterDTO dto, PaginationDTO paginDTO) {
    var pagin = PaginationUtility.createPageable(
            paginDTO.getPage(),
            paginDTO.getLimit(),
            paginDTO.getSortBy(),
            paginDTO.getSortOrder()
    );

    var data = dapilRepository.findByFilter(
            dto.getNamaDapil(),
            dto.getProvinsi(),
            dto.getJumlahKursi(),
            pagin
    );

    return StreamSupport.stream(data.spliterator(), false)
            .map(this::toDapilResponseFrom)
            .collect(Collectors.toList());
  }

  @Override
  public Integer getTotalDapil(DapilFilterDTO dto) {
    return dapilRepository.countByFilter(
            dto.getNamaDapil(),
            dto.getProvinsi(),
            dto.getJumlahKursi()
    );
  }

  @Override
  public DapilResponseDTO getDetailDapilById(UUID id) {
    var data = dapilRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dapil tidak ditemukan"));
    return toDapilResponseFrom(data);
  }

  @Override
  @Transactional
  public void createDapil(DapilRequestDTO req) {
    validatorService.validate(req);

    var timeNow = CurrentTimestampUtility.getLocalDateTime();
    var dapil  = Dapil.builder()
            .namaDapil(req.getNamaDapil())
            .provinsi(req.getProvinsi())
            .jumlahKursi(req.getJumlahKursi())
            .wilayahDapilList(req.getWilayahDapilList())
            .createdAt(timeNow)
            .updatedAt(timeNow)
            .build();

    dapilRepository.save(dapil);
  }

  @Override
  @Transactional
  public void updateDapil(UUID id, DapilRequestDTO req) {
    validatorService.validate(req);

    var dapil = dapilRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dapil tidak ditemukan"));

    dapil.setNamaDapil(
            req.getNamaDapil() != null ? req.getNamaDapil() : dapil.getNamaDapil()
    );

    dapil.setProvinsi(
            req.getProvinsi() != null ? req.getProvinsi() : dapil.getProvinsi()
    );

    dapil.setJumlahKursi(
            req.getJumlahKursi() != 0 ? req.getJumlahKursi() : dapil.getJumlahKursi()
    );

    dapil.setWilayahDapilList(
            req.getWilayahDapilList() !=  null ? req.getWilayahDapilList() : dapil.getWilayahDapilList()
    );

    dapil.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    dapilRepository.save(dapil);
  }

  @Override
  @Transactional
  public void activateDapil(UUID id) {
    var dapil = dapilRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dapil tidak ditemukan"));

    dapil.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    var deletedAt = dapil.getDeletedAt();
    if(deletedAt != null) {
      dapil.setDeletedAt(null);
    }

    dapilRepository.save(dapil);
  }

  @Override
  @Transactional
  public void deactivateDapil(UUID id) {
    var dapil = dapilRepository.
            findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dapil tidak ditemukan"));

    dapil.setUpdatedAt(CurrentTimestampUtility.getLocalDateTime());

    var deletedAt = dapil.getDeletedAt();
    if(deletedAt == null) {
      dapil.setDeletedAt(CurrentTimestampUtility.getLocalDateTime());
    }

    dapilRepository.save(dapil);
  }
}
