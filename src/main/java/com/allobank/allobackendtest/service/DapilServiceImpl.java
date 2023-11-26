package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DapilServiceImpl implements DapilService{

    private DapilRepository dapilRepository;
    @Override
    public Dapil createDapil(Dapil dapil) {
        System.out.println("Before save: " + dapil);
        Dapil savedDapil = dapilRepository.save(dapil);
        System.out.println("After save: " + savedDapil);
        return savedDapil;
    }

    @Override
    public Dapil getDapilById(UUID dapilId) {
        return dapilRepository.findById(dapilId)
                .orElseThrow(() -> new RuntimeException("Dapil not found with id " + dapilId));
    }

    @Override
    public List<Dapil> getAllDapil() {
        return dapilRepository.findAll();
    }

    @Override
    public List<Dapil> getAllDapilSort(Sort sort) {
        return dapilRepository.findAll(sort);
    }

    @Override
    public Dapil updateDapil(Dapil dapil) {
        return dapilRepository.findById(dapil.getId())
                .map(dataDapil -> {
                    dataDapil.setNamaDapil(dapil.getNamaDapil());
                    dataDapil.setWilayahDapilList(dapil.getWilayahDapilList());
                    dataDapil.setProvinsi(dapil.getProvinsi());
                    dataDapil.setJumlahKursi(dapil.getJumlahKursi());
                    return dataDapil;
                })
                .orElseThrow(() -> new RuntimeException("Dapil not found with id "+dapil.getId()));
    }

    @Override
    public void deleteDapil(UUID dapilId) {
        dapilRepository.deleteById(dapilId);
    }
}
