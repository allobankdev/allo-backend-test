package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.*;

import java.util.List;
import java.util.UUID;

public interface CalegService {
  List<CalegResponseDTO> getListCaleg(CalegFilterDTO dto, PaginationDTO pagin);

  Integer getTotalCaleg(CalegFilterDTO dto);

  CalegResponseDTO getDetailCalegById(UUID id);
  void createCaleg(CalegRequestDTO req);
  void updateCaleg(UUID id, CalegRequestDTO req);

  void activateCaleg(UUID id);

  void deactivateCaleg(UUID id);
}
