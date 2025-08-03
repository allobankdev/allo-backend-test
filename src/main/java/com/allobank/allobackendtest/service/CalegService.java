package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.Request.CalegRequestDTO;
import com.allobank.allobackendtest.dto.Response.CalegResponseDTO;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.mapper.CalegMapper;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalegService {

    private final CalegRepository calegRepository;
    private final DapilRepository dapilRepository;
    private final PartaiRepository partaiRepository;

    public List<CalegResponseDTO> getAllCaleg(UUID dapilId, UUID partaiId, String sortBy) {
        String defaultSortField = "nomorUrut";
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : defaultSortField;
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortField.startsWith("-")) {
            direction = Sort.Direction.DESC;
            sortField = sortField.substring(1); // hapus "-" untuk field
        }

        List<String> allowedSortFields = List.of("nomorUrut", "nama", "jenisKelamin");
        if (!allowedSortFields.contains(sortField)) {
            throw new IllegalArgumentException("Kolom sort tidak valid. Gunakan: " + allowedSortFields);
        }

        Sort sort = Sort.by(direction, sortField);
        List<CalegEntity> calegs;

        if (dapilId != null && partaiId != null) {
            calegs = calegRepository.findByDapilIdAndPartaiId(dapilId, partaiId, sort);
        } else if (dapilId != null) {
            calegs = calegRepository.findByDapilId(dapilId, sort);
        } else if (partaiId != null) {
            calegs = calegRepository.findByPartaiId(partaiId, sort);
        } else {
            calegs = calegRepository.findAll(sort);
        }

        if (calegs.isEmpty()) {
            throw new NoSuchElementException("Tidak ada data caleg ditemukan dengan filter tersebut");
        }

        return calegs.stream().map(CalegMapper::toDTO).toList();
    }

    public CalegResponseDTO getById(UUID id) {
        CalegEntity entity = calegRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Caleg not found"));
        return CalegMapper.toDTO(entity);
    }

    public CalegResponseDTO create(CalegRequestDTO request) {
        CalegEntity entity = new CalegEntity();
        entity.setId(UUID.randomUUID());
        entity.setNama(request.getNama());
        entity.setNomorUrut(request.getNomorUrut());
        entity.setJenisKelamin(request.getJenisKelamin());

        DapilEntity dapil = dapilRepository.findById(request.getDapilId())
                .orElseThrow(() -> new NoSuchElementException("Dapil not found"));
        PartaiEntity partai = partaiRepository.findById(request.getPartaiId())
                .orElseThrow(() -> new NoSuchElementException("Partai not found"));

        entity.setDapil(dapil);
        entity.setPartai(partai);

        CalegEntity saved = calegRepository.save(entity);
        return CalegMapper.toDTO(saved);
    }

    public CalegResponseDTO update(UUID id, CalegRequestDTO request) {
        CalegEntity entity = calegRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Caleg not found"));

        entity.setNama(request.getNama());
        entity.setNomorUrut(request.getNomorUrut());
        entity.setJenisKelamin(request.getJenisKelamin());

        if (!entity.getDapil().getId().equals(request.getDapilId())) {
            DapilEntity dapil = dapilRepository.findById(request.getDapilId())
                    .orElseThrow(() -> new NoSuchElementException("Dapil not found"));
            entity.setDapil(dapil);
        }

        if (!entity.getPartai().getId().equals(request.getPartaiId())) {
            PartaiEntity partai = partaiRepository.findById(request.getPartaiId())
                    .orElseThrow(() -> new NoSuchElementException("Partai not found"));
            entity.setPartai(partai);
        }

        CalegEntity updated = calegRepository.save(entity);
        return CalegMapper.toDTO(updated);
    }

    public void delete(UUID id) {
        if (!calegRepository.existsById(id)) {
            throw new NoSuchElementException("Caleg not found");
        }
        calegRepository.deleteById(id);
    }
}
