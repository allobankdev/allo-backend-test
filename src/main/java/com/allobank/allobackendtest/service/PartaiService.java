package com.allobank.allobackendtest.service;

import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.entity.Partai;


import java.util.List;

@Service
public interface PartaiService {
    Partai savePartai(Partai dapil);
    Partai getPartaiById(Long id);
    List<Partai> getAllPartais();
    void deletePartai(Long id);
}
