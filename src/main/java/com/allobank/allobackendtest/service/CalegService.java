package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.CalegRequest;
import com.allobank.allobackendtest.exception.EntityNotFoundException;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for managing Caleg entities.
 */

@Service
@Slf4j
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    @Autowired
    private DapilRepository dapilRepository;

    /**
     * Retrieves all Caleg based on Dapil and Partai names,
     * and organizes the result set in order.
     *
     * @param namaDapil    Name of the Dapil for Caleg search
     * @param namaPartai   Name of the Partai for Caleg search
     * @param sortDirection Sorting direction, "ASC" or "DESC"
     * @param pageable     Pageable object for pagination
     * @return Page of Caleg matching the search criteria
     */

    public Page<Caleg> getAllCalegs(String namaDapil, String namaPartai, String sortDirection, Pageable pageable) {
        if (sortDirection == null || (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC"))) {
            List<Caleg> calegList = calegRepository.findCalegByDapilNameAndPartaiNameWithoutSort(namaDapil, namaPartai);
            return new PageImpl<>(calegList, pageable, calegList.size());
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                "ASC".equalsIgnoreCase(sortDirection) ? Sort.by("nomor_urut").ascending() : Sort.by("nomor_urut").descending());

        return calegRepository.findCalegWithSorting(namaDapil, namaPartai, sortedPageable);
    }


    /**
     * Adds a new Caleg based on the request.
     *
     * @param request CalegRequest object containing new Caleg data
     * @return Caleg that has been added
     * @throws IllegalArgumentException If Dapil ID, Partai ID, or Name is invalid
     * @throws EntityNotFoundException   If Dapil or Partai is not found
     */

    public Caleg addCaleg(CalegRequest request) {
        log.info("isi request : {}", request);

        if (request.getDapilId() == null || request.getPartaiId() == null || request.getNama() == null) {
            throw new IllegalArgumentException("Dapil ID, Partai ID, dan Nama must be filled.");
        }

        Dapil dapil = dapilRepository.findById(request.getDapilId())
                .orElseThrow(() -> new EntityNotFoundException("Dapil with ID " + request.getDapilId() + " not found."));
        Partai partai = partaiRepository.findById(request.getPartaiId())
                .orElseThrow(() -> new EntityNotFoundException("Partai with ID " + request.getPartaiId() + " not found."));

        if (calegRepository.existsByNomorUrut(request.getNomorUrut())) {
            throw new IllegalArgumentException("Nomor urut " + request.getNomorUrut() + " already registered. please try with another one!!");
        }

        Caleg entity = new Caleg();
        entity.setDapil(dapil);
        entity.setPartai(partai);
        entity.setNama(request.getNama());
        entity.setNomorUrut(request.getNomorUrut());
        entity.setJenisKelamin(request.getJenisKelamin());

        return calegRepository.save(entity);
    }
}
