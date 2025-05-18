package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.util.RequestGetCaleg;
import com.allobank.allobackendtest.util.RequestBodyCaleg;
import com.allobank.allobackendtest.util.dto.CalegResponseDTO;

public interface CalegService {

    List<Caleg> getAllCaleg(RequestGetCaleg caleg);
    Caleg getCalegById(UUID id);
    CalegResponseDTO createCaleg(RequestBodyCaleg caleg);
    CalegResponseDTO updateCaleg(UUID id, RequestBodyCaleg caleg);
    void deleteCaleg(UUID id);
}
