package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.mapper.CalegMapper;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing Caleg (Legislative Candidates).
 */
@Service
public class CalegService extends BaseService<CalegEntity, CalegDto, Caleg, UUID, CalegRepository> {

    private final DapilRepository dapilRepository;
    private final PartaiRepository partaiRepository;
    private final CalegMapper calegMapper;


    public CalegService(
            CalegRepository repository,
            PartaiRepository partaiRepository,
            DapilRepository dapilRepository,
            CalegMapper mapper) {
        super(repository, mapper);
        this.dapilRepository = dapilRepository;
        this.partaiRepository = partaiRepository;
        this.calegMapper = mapper;
    }

    /**
     * Overrides the create method to provide additional validation.
     *
     * @param dto DTO containing the Caleg data.
     * @return The created Caleg.
     */
    @Override
    @Transactional
    public Caleg create(CalegDto dto) {
        CalegEntity entity = calegMapper.toEntity(dto);

        // resolve UUID -> Entity and validate
        entity.setDapil(dapilRepository.findById(dto.getDapil())
                .orElseThrow(() -> new EntityNotFoundException("Dapil tidak ditemukan")));
        entity.setPartai(partaiRepository.findById(dto.getPartai())
                .orElseThrow(() -> new EntityNotFoundException("Partai tidak ditemukan")));

        return calegMapper.toResponse(repository.save(entity));
    }
    /**
     * Overrides the update method to provide additional validation.
     *
     * @param id  ID of the Caleg to update.
     * @param dto DTO containing the updated data.
     * @return The updated Caleg.
     */
    @Override
    @Transactional
    public Caleg update(UUID id, CalegDto dto) {
        CalegEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caleg tidak ditemukan"));
        calegMapper.updateFromRequest(dto, entity);

        // validate set dapil
        if (dto.getDapil() != null) {
            entity.setDapil(dapilRepository.findById(dto.getDapil())
                    .orElseThrow(() -> new EntityNotFoundException("Dapil tidak ditemukan")));
        }
        // validate set partai
        if (dto.getPartai() != null) {
            entity.setPartai(partaiRepository.findById(dto.getPartai())
                    .orElseThrow(() -> new EntityNotFoundException("Partai tidak ditemukan")));
        }

        return calegMapper.toResponse(repository.save(entity));
    }

    /**
     * Retrieves a list of Caleg by their name.
     *
     * @param nama Name of the Caleg to search for.
     * @return List of Caleg matching the name.
     */
    public List<Caleg> getByNama(String nama) {
        return calegMapper.toResponseList(repository.findByNama(nama));
    }
}

