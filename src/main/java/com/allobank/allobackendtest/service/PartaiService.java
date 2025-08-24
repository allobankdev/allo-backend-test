package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.request.PartaiRequestDTO;
import com.allobank.allobackendtest.dto.response.PartaiResponseDTO;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
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
public class PartaiService {

    @Autowired
    private PartaiRepository partaiRepository;

    public Page<PartaiResponseDTO> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Partai> partaiPage =  partaiRepository.findAll(pageable);

        Page<PartaiResponseDTO> partaiResponseDTOPage = partaiPage.map(p -> {
            PartaiResponseDTO partaiResponseDTO = new PartaiResponseDTO();
            partaiResponseDTO.setNamaPartai(p.getNamaPartai());
            partaiResponseDTO.setNomorUrut(p.getNomorUrut());
            return partaiResponseDTO;
        });
        return partaiResponseDTOPage;
    }

    public Optional<PartaiResponseDTO> findById(UUID id) {
        Optional<Partai> partai = partaiRepository.findById(id);

        if (partai.isPresent()) {
            Optional<PartaiResponseDTO> partaiResponseDTO = partai.map(p -> {
                PartaiResponseDTO dto = new PartaiResponseDTO();
                dto.setNamaPartai(p.getNamaPartai());
                dto.setNomorUrut(p.getNomorUrut());
                return dto;
            });
            return partaiResponseDTO;
        }
        return Optional.empty();
    }

    public PartaiResponseDTO save(PartaiRequestDTO partaiRequestDTO) {
        if (partaiRepository.existsByNamaPartai(partaiRequestDTO.getNamaPartai())) {
            throw new EntityExistsException("Partai name already exist");
        }

        Partai partai = new Partai();
        partai.setNamaPartai(partaiRequestDTO.getNamaPartai());
        partai.setNomorUrut(partaiRequestDTO.getNomorUrut());

        Partai savedPartai = partaiRepository.save(partai);

        PartaiResponseDTO partaiResponseDTO = new PartaiResponseDTO();
        partaiResponseDTO.setNamaPartai(savedPartai.getNamaPartai());
        partaiResponseDTO.setNomorUrut(savedPartai.getNomorUrut());
        return partaiResponseDTO;
    }

    public PartaiResponseDTO update(PartaiRequestDTO partaiRequestDTO, UUID id) {
        Partai partai = partaiRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Partai not found"));

        if(partaiRepository.existsByNamaPartai(partaiRequestDTO.getNamaPartai()) &&
                !partai.getNamaPartai().equals(partaiRequestDTO.getNamaPartai())) {
            throw new EntityExistsException("Partai name already exist");
        }

        partai.setNamaPartai(partaiRequestDTO.getNamaPartai());
        partai.setNomorUrut(partaiRequestDTO.getNomorUrut());

        Partai savedPartai = partaiRepository.save(partai);

        PartaiResponseDTO partaiResponseDTO = new PartaiResponseDTO();
        partaiResponseDTO.setNamaPartai(savedPartai.getNamaPartai());
        partaiResponseDTO.setNomorUrut(savedPartai.getNomorUrut());

        return partaiResponseDTO;
    }

    public void deleteById(UUID id) {
        partaiRepository.deleteById(id);
    }
}
