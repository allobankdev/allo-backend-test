package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service class for managing Dapil entities.
 */

@Service
@Slf4j
public class DapilService {
    private static final Logger logger = LoggerFactory.getLogger(DapilService.class);

    @Autowired
    private DapilRepository dapilRepository;


    /**
     * Retrieves all Dapil entities from the repository.
     *
     * @return a list of all Dapil entities.
     */

    public List<Dapil> getAllDapils() {
        return dapilRepository.findAll();
    }


    /**
     * Adds a new Dapil entity to the repository.
     *
     * @param dapil the Dapil entity to be added.
     * @return the saved Dapil entity.
     * @throws IllegalArgumentException if the Dapil name is null or empty, or if a Dapil with the same name already exists.
     */

    public Dapil addDapil(Dapil dapil) {
        if (dapil.getNamaDapil() == null || dapil.getNamaDapil().isEmpty()) {
            throw new IllegalArgumentException("Dapil name cannot be null or empty");
        }

        List<Dapil> existingDapils = dapilRepository.findByNamaDapilIgnoreCase(dapil.getNamaDapil());
        if (!existingDapils.isEmpty()) {
            throw new IllegalArgumentException("Dapil already registered, try with another one");
        }

        logger.info("Adding Dapil: {}", dapil);
        return dapilRepository.save(dapil);
    }



    /**
     * Retrieves a Dapil entity by its ID.
     *
     * @param id the ID of the Dapil entity to retrieve.
     * @return an Optional containing the Dapil entity if found, otherwise empty.
     */

    public Optional<Dapil> getDapilById(Long id) {
        return dapilRepository.findById(id);
    }
}
