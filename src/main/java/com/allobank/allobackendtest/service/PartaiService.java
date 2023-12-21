package com.allobank.allobackendtest.service;


import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.utils.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.zip.DataFormatException;

@Service
public class PartaiService {
    @Autowired
    private  PartaiRepository partaiRepository;


    public Partai savePartai(Partai partai) {
        // Validasi: Nama Partai harus diisi
        if (Objects.isNull(partai.getNamaPartai()) || partai.getNamaPartai().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama Partai harus diisi");
        }

        // Tambahan validasi lainnya sesuai kebutuhan

        return partaiRepository.save(partai);
    }

    public Partai getPartaiById(String id) {
        return partaiRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Partai dengan ID " + id + " tidak ditemukan"));
    }

    public List<Partai> getAllPartai() {
        return partaiRepository.findAll();
    }

    public Partai updatePartai(Partai partai) {
        if (partaiRepository.findById(partai.getId()).isPresent())
            return savePartai(partai);
        else throw new DataNotFoundException("Data with id" + partai.getId()+ "is not found");
    }

    public void deletePartai(String id) {
        // Validasi: Partai harus sudah ada sebelum dihapus
        if (!partaiRepository.existsById(id)) {
            throw new NoSuchElementException("Partai dengan ID " + id + " tidak ditemukan");
        }

        partaiRepository.deleteById(id);
    }

    public Page<Partai> getPartaiPerPage(Pageable pageable) {
        return partaiRepository.findAll(pageable);
    }

    public List<Partai> searchPartai(String namaPartai) {
        return partaiRepository.findPartaiByNamaPartai(namaPartai);
    }
}

