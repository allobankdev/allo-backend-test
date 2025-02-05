package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;

import java.util.List;

public interface CalegService {

    List<Caleg> listCalegs(String namaDapil, String namaPartai, String sortOrder);
}
