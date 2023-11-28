package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.*;

import java.util.List;
import java.util.UUID;

public interface DapilService {
  List<DapilResponseDTO> getListDapil(DapilFilterDTO dto, PaginationDTO pagin);

  Integer getTotalDapil(DapilFilterDTO dto);

  DapilResponseDTO getDetailDapilById(UUID id);
  void createDapil(DapilRequestDTO req);
  void updateDapil(UUID id, DapilRequestDTO req);

  void activateDapil(UUID id);

  void deactivateDapil(UUID id);
}
