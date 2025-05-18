package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.DapilRepo;
import com.allobank.allobackendtest.repository.PartaiRepo;
import com.allobank.allobackendtest.util.RequestBodyCaleg;
import com.allobank.allobackendtest.util.dto.CalegResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepo;
import com.allobank.allobackendtest.util.RequestGetCaleg;

@Service
public class CalegServiceImpl implements CalegService {

    @Autowired
    private CalegRepo calegRepo;

    @Autowired
    private DapilRepo dapilRepo;

    @Autowired
    private PartaiRepo partaiRepo;
    
    @Override
    public List<Caleg> getAllCaleg(RequestGetCaleg caleg) {
        return calegRepo.findByDapilIdAndPartaiIdOrderByNomorUrutAsc(caleg.getDapil(), caleg.getPartai());
    }

    @Override
    public Caleg getCalegById(UUID id) {
        return calegRepo.findById(id).orElse(null);
    }

    @Override
    public CalegResponseDTO createCaleg(RequestBodyCaleg caleg) {
        Dapil d = dapilRepo.getReferenceById(UUID.fromString(caleg.getDapil()));
        Partai pa = partaiRepo.getReferenceById(UUID.fromString(caleg.getPartai()));

        Caleg c = new Caleg();
        c.setDapil(d);
        c.setPartai(pa);
        c.setNama(caleg.getNama());
        c.setNomorUrut(caleg.getNomorUrut());
        c.setJenisKelamin(JenisKelamin.valueOf(caleg.getJenisKelamin()));

        Caleg calegSave = calegRepo.save(c);

        return new CalegResponseDTO(calegSave);
    }

    @Override
    public CalegResponseDTO updateCaleg(UUID id, RequestBodyCaleg caleg) {
        Caleg c = calegRepo.findById(id).orElse(null);

        if (caleg.getNama() != null) {
            assert c != null;
            c.setNama(caleg.getNama());
        }

        if (caleg.getNomorUrut() != null) {
            assert c != null;
            c.setNomorUrut(caleg.getNomorUrut());
        }

        if (caleg.getDapil() != null) {
            Dapil dapil = dapilRepo.findById(UUID.fromString(caleg.getDapil()))
                    .orElseThrow(() -> new RuntimeException("Dapil not found"));
            assert c != null;
            c.setDapil(dapil);
        }

        if (caleg.getPartai() != null) {
            Partai partai = partaiRepo.findById(UUID.fromString(caleg.getPartai()))
                    .orElseThrow(() -> new RuntimeException("Partai not found"));
            assert c != null;
            c.setPartai(partai);
        }

        if (caleg.getJenisKelamin() != null) {
            assert c != null;
            c.setJenisKelamin(JenisKelamin.valueOf(caleg.getJenisKelamin()));
        }

        assert c != null;
        Caleg calegUpdate = calegRepo.save(c);
        return new CalegResponseDTO(calegUpdate);
    }

    @Override
    public void deleteCaleg(UUID id) {
        calegRepo.deleteById(id);
    }
}
