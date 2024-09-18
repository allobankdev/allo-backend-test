package com.allobank.allobackendtest.service;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.entity.Dapil;


import java.util.List;


@Service
public interface DapilService {
    Dapil saveDapil(Dapil dapil);
    Dapil getDapilById(Long id);
    List<Dapil> getAllDapils();
    void deleteDapil(Long id);
}
