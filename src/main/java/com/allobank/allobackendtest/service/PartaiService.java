package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.PaginationDTO;
import com.allobank.allobackendtest.dto.PartaiFilterDTO;
import com.allobank.allobackendtest.dto.PartaiRequestDTO;
import com.allobank.allobackendtest.dto.PartaiResponseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface PartaiService {
  List<PartaiResponseDTO> getListPartai(PartaiFilterDTO dto, PaginationDTO pagin);

  Integer getTotalPartai(PartaiFilterDTO dto);

  PartaiResponseDTO getDetailPartaiById(UUID id);
  void createPartai(PartaiRequestDTO req);
  void updatePartai(UUID id, PartaiRequestDTO req);

  void activatePartai(UUID id);

  void deactivatePartai(UUID id);
}
