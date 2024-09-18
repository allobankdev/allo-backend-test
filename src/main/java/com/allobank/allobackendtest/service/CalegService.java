package com.allobank.allobackendtest.service;

import java.util.List;

import com.allobank.allobackendtest.dto.CalegFilterDTO;
import com.allobank.allobackendtest.entity.Caleg;

public interface CalegService {
    List<Caleg> getAllCaleg(CalegFilterDTO filterDTO); // Correct method name
}
