package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.exception.PartaiNotFoundException;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing political parties.
 */
@Service
public class PartaiService {

    private final PartaiRepository partaiRepository;

    @Autowired
    public PartaiService(PartaiRepository partaiRepository) {
        this.partaiRepository = partaiRepository;
    }

    /**
     * Retrieves all political parties.
     *
     * @return a list of all political parties.
     */
    public List<Partai> getAllParties() {
        return partaiRepository.findAll();
    }

    /**
     * Adds a new Partai entity to the repository.
     *
     * @param partai the Partai entity to be added.
     * @return the saved Partai entity.
     * @throws IllegalArgumentException if the Partai name is null or empty, or if a Partai with the same name already exists.
     */
    public Partai addPartai(Partai partai) {

        if (partai.getNamaPartai() == null || partai.getNamaPartai().isEmpty()) {
            throw new IllegalArgumentException("Partai name cannot be null or empty");
        }

        List<Partai> existingPartais = partaiRepository.findByNamaPartaiIgnoreCase(partai.getNamaPartai());
        if (!existingPartais.isEmpty()) {
            throw new IllegalArgumentException("Partai already registered, try with another one");
        }

        return partaiRepository.save(partai);
    }


    /**
     * Retrieves a political party by its ID.
     *
     * @param id the ID of the political party.
     * @return the found political party.
     * @throws PartaiNotFoundException if the partai is not found.
     */
    public Partai getPartaiById(Long id) {
        return partaiRepository.findById(id)
                .orElseThrow(() -> new PartaiNotFoundException(id));
    }
}
