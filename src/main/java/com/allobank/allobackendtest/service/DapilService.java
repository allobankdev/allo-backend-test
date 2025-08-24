package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.request.DapilRequestDTO;
import com.allobank.allobackendtest.dto.response.DapilResponseDTO;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DapilService {

    @Autowired
    private DapilRepository dapilRepository;

    public Page<DapilResponseDTO> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Dapil> dapilPage =  dapilRepository.findAll(pageable);

        Page<DapilResponseDTO> dapilResponseDTOPage = dapilPage.map(e -> {
            DapilResponseDTO dapilResponseDTO = new DapilResponseDTO();
            dapilResponseDTO.setNamaDapil(e.getNamaDapil());
            dapilResponseDTO.setProvinsi(e.getProvinsi());
            dapilResponseDTO.setWilayahDapilList(e.getWilayahDapilList());
            dapilResponseDTO.setJumlahKursi(e.getJumlahKursi());
            return dapilResponseDTO;
        });
        return dapilResponseDTOPage;

    }

    public Optional<DapilResponseDTO> findById(UUID id) {
        Optional<Dapil> dapil = dapilRepository.findById(id);

        if (dapil.isPresent()){
            Optional<DapilResponseDTO> dapilResponseDTO = dapil.map(e -> {
                DapilResponseDTO dto = new DapilResponseDTO();
                dto.setNamaDapil(e.getNamaDapil());
                dto.setProvinsi(e.getProvinsi());
                dto.setWilayahDapilList(e.getWilayahDapilList());
                dto.setJumlahKursi(e.getJumlahKursi());
                return dto;
            });
            return dapilResponseDTO;
        }
        return Optional.empty();
    }

    public DapilResponseDTO save(DapilRequestDTO dapilRequestDTO) {
        if (dapilRepository.existsByNamaDapil(dapilRequestDTO.getNamaDapil())){
            throw new EntityExistsException("Dapil name already exist");
        }

        Dapil dapil = new Dapil();
        dapil.setNamaDapil(dapilRequestDTO.getNamaDapil());
        dapil.setProvinsi(dapilRequestDTO.getProvinsi());
        dapil.setWilayahDapilList(dapilRequestDTO.getWilayahDapilList());
        dapil.setJumlahKursi(dapilRequestDTO.getJumlahKursi());

        Dapil savedDapil = dapilRepository.save(dapil);

        DapilResponseDTO dapilResponseDTO = new DapilResponseDTO();
        dapilResponseDTO.setNamaDapil(savedDapil.getNamaDapil());
        dapilResponseDTO.setProvinsi(savedDapil.getProvinsi());
        dapilResponseDTO.setWilayahDapilList(savedDapil.getWilayahDapilList());
        dapilResponseDTO.setJumlahKursi(savedDapil.getJumlahKursi());
        return dapilResponseDTO;
    }

    public DapilResponseDTO update(DapilRequestDTO dapilRequestDTO, UUID id) {
        Dapil dapil = dapilRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Dapil not found"));

        if (dapilRepository.existsByNamaDapil(dapilRequestDTO.getNamaDapil()) &&
                !dapil.getNamaDapil().equals(dapilRequestDTO.getNamaDapil())){
            throw new EntityExistsException("Dapil name already exists");
        }

        dapil.setNamaDapil(dapilRequestDTO.getNamaDapil());
        dapil.setProvinsi(dapilRequestDTO.getProvinsi());
        dapil.setWilayahDapilList(dapilRequestDTO.getWilayahDapilList());
        dapil.setJumlahKursi(dapilRequestDTO.getJumlahKursi());

        Dapil savedDapil = dapilRepository.save(dapil);

        DapilResponseDTO dapilResponseDTO = new DapilResponseDTO();
        dapilResponseDTO.setNamaDapil(savedDapil.getNamaDapil());
        dapilResponseDTO.setProvinsi(savedDapil.getProvinsi());
        dapilResponseDTO.setWilayahDapilList(savedDapil.getWilayahDapilList());
        dapilResponseDTO.setJumlahKursi(savedDapil.getJumlahKursi());

        return dapilResponseDTO;
    }

    public void deleteById(UUID id) {
        dapilRepository.deleteById(id);
    }
}
