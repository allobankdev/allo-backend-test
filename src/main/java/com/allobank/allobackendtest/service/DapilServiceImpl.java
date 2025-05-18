package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepo;
import com.allobank.allobackendtest.util.dto.DapilRequestDTO;
import com.allobank.allobackendtest.util.dto.DapilResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DapilServiceImpl implements DapilService{

    @Autowired
    private DapilRepo dapilRepo;

    @Override
    public List<Dapil> getAllDapil() {
        return dapilRepo.findAll();
    }

    @Override
    public DapilResponseDTO getDapilById(UUID id) {
        Dapil d = dapilRepo.findById(id).orElse(null);
        return new DapilResponseDTO(d);
    }

    @Override
    public DapilResponseDTO updateDapil(UUID id, DapilRequestDTO dapil) {
        Dapil d = dapilRepo.findById(id).orElse(null);

        if (dapil.getNamaDapil() != null) {
            assert d != null;
            d.setNamaDapil(dapil.getNamaDapil());
        }

        if (dapil.getWilayahDapilList() != null) {
            assert d != null;
            d.setWilayahDapilList(dapil.getWilayahDapilList());
        }

        if (dapil.getProvinsi() != null) {
            assert d != null;
            d.setProvinsi(dapil.getProvinsi());
        }

        assert d != null;
        if (dapil.getJumlahKursi() != d.getJumlahKursi()) {
            d.setJumlahKursi(dapil.getJumlahKursi());
        }

        Dapil updateDapil = dapilRepo.save(d);

        return new DapilResponseDTO(updateDapil);
    }

    @Override
    public DapilResponseDTO createDapil(DapilRequestDTO dapil) {
        Dapil d = new Dapil();

        d.setNamaDapil(dapil.getNamaDapil());
        d.setProvinsi(dapil.getProvinsi());
        d.setJumlahKursi(dapil.getJumlahKursi());
        d.setWilayahDapilList(dapil.getWilayahDapilList());

        Dapil saveDapil = dapilRepo.save(d);

        return new DapilResponseDTO(saveDapil);
    }

    @Override
    public void deleteDapil(UUID id) {
        dapilRepo.deleteById(id);
    }
}
