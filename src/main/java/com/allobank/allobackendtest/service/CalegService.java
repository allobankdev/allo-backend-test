package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.exception.CalegNotFoundException;
import com.allobank.allobackendtest.model.Caleg;

import java.util.List;

public interface CalegService {
    List<Caleg> getAllCalegs(String namaDapil, String namaPartai, Integer page, Integer size, String sortBy) throws CalegNotFoundException;
}