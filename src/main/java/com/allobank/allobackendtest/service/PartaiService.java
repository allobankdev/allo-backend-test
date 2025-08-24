package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.PartaiDto;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.mapper.PartaiMapper;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class PartaiService extends BaseService<PartaiEntity, PartaiDto, Partai, UUID, PartaiRepository> {
    private final PartaiMapper partaiMapper;

    public PartaiService(PartaiRepository repository, PartaiMapper mapper) {
        super(repository, mapper);
        this.partaiMapper = mapper;
    }
    /**
     * Method to find Partai by name
     * custom service method for finding Partai by name
     * @param nama
     * @return Partai
    */ 
    public Partai findByNamaPartai(String nama){
        return partaiMapper.toResponse(
                repository.findBynamaPartai(nama)
                        .orElseThrow(()-> new RuntimeException("No Found Partai"))
        );
    }
}
