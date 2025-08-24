package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.common.exception.BadRequestException;
import com.allobank.allobackendtest.common.exception.ResourceNotFoundException;
import com.allobank.allobackendtest.dto.DapilDto;

import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.mapper.DapilMapper;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for managing Dapil (Electoral District).
 */
@Service
public class DapilService extends BaseService<DapilEntity, DapilDto, Dapil, UUID, DapilRepository> {

    private final DapilMapper dapilMapper;
    private final DapilRepository dapilRepository;

    public DapilService(DapilRepository repository, DapilMapper mapper) {
        super(repository, mapper);
        this.dapilMapper = mapper;
        this.dapilRepository = repository;
    }

    /**
     * Overrides the create method to provide additional validation.
     *
     * @param dto DTO containing the Dapil data.
     * @return The created Dapil.
     */
    @Override
    @Transactional
    public Dapil create(DapilDto dto){
        DapilEntity entity = dapilMapper.toEntity(dto);
        return dapilMapper.toResponse(dapilRepository.save(entity));
    }

    /**
     * Overrides the update method to provide additional validation.
     *
     * @param id  ID of the Dapil to update.
     * @param dto DTO containing the updated data.
     * @return The updated Dapil.
     */
    @Override
    @Transactional
    public Dapil update(UUID id, DapilDto dto) {
        DapilEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Dapil"));

        if (dto.getJumlahKursi() != null && dto.getJumlahKursi() <= 0) {
            throw new BadRequestException("Jumlah kursi harus lebih besar dari 0");
        }

        dapilMapper.updateFromRequest(dto, entity);

        return dapilMapper.toResponse(repository.save(entity));
    }

    /**
     * Get Dapil by name.
     *
     * @param nama
     * @return
     */
    public Dapil getByNama(String nama) {
        return mapper.toResponse(
                repository.findByNamaDapil(nama)
                        .orElseThrow(() -> new RuntimeException("Dapil tidak ditemukan"))
        );
    }
}