package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.model.enums.JenisKelamin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CalegService {

    private final List<Caleg> calegList = new ArrayList<>();

    public CalegService() {
        Caleg caleg1 = new Caleg();
        caleg1.setId(UUID.randomUUID());
        caleg1.setNama("Caleg 1");
        caleg1.setDapil(new Dapil(1L, "Dapil 1"));
        caleg1.setPartai(new Partai(1L, "Partai A"));
        caleg1.setNomorUrut(1);
        caleg1.setJenisKelamin(JenisKelamin.PRIA);

        Caleg caleg2 = new Caleg();
        caleg2.setId(UUID.randomUUID());
        caleg2.setNama("Caleg 2");
        caleg2.setDapil(new Dapil(2L, "Dapil 2"));
        caleg2.setPartai(new Partai(2L, "Partai B"));
        caleg2.setNomorUrut(2);
        caleg2.setJenisKelamin(JenisKelamin.WANITA);

        calegList.add(caleg1);
        calegList.add(caleg2);
    }

    public List<Caleg> getAllCaleg(String sortBy) {
        return calegList.stream()
                .sorted((sortBy.equalsIgnoreCase("nomor_urut"))
                        ? Comparator.comparing(Caleg::getNomorUrut)
                        : Comparator.comparing(Caleg::getNama))
                .collect(Collectors.toList());
    }

    public List<Caleg> getFilteredCaleg(Long dapilId, Long partaiId, String sortBy) {
        return calegList.stream()
                .filter(caleg -> caleg.getDapil().getId().equals(dapilId) &&
                        caleg.getPartai().getId().equals(partaiId))
                .sorted((sortBy.equalsIgnoreCase("nomor_urut"))
                        ? Comparator.comparing(Caleg::getNomorUrut)
                        : Comparator.comparing(Caleg::getNama))
                .collect(Collectors.toList());
    }

    public List<Caleg> getCalegsByDapil(String dapil) {
        return calegList.stream()
                .filter(caleg -> caleg.getDapil().getNama().equalsIgnoreCase(dapil))
                .collect(Collectors.toList());
    }

    public List<Caleg> getCalegsByPartai(String partai) {
        return calegList.stream()
                .filter(caleg -> caleg.getPartai().getNama().equalsIgnoreCase(partai))
                .collect(Collectors.toList());
    }

    public Optional<Caleg> getCalegById(UUID id) {
        return calegList.stream()
                .filter(caleg -> caleg.getId().equals(id))
                .findFirst();
    }

    public void addCaleg(Caleg caleg) {
        calegList.add(caleg);
    }
}
