package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PartaiServiceImpl implements PartaiService {

    private PartaiRepository partaiRepository;
    @Override
    public Partai createPartai(Partai partai) {
        return partaiRepository.save(partai);
    }

    @Override
    public Partai getPartaiById(UUID partaiId) {
        return partaiRepository.findById(partaiId)
                .orElseThrow(()-> new RuntimeException("Partai not found with id "+partaiId));
    }


    @Override
    public List<Partai> getAllPartai() {
        return partaiRepository.findAll();
    }

    @Override
    public List<Partai> getAllPartaiSort(Sort sort) {
        return partaiRepository.findAll(sort);
    }

    @Override
    public Partai updatePartai(Partai partai) {
        return partaiRepository.findById(partai.getId())
                .map(dataPartai -> {
                    dataPartai.setNamaPartai(partai.getNamaPartai());
                    dataPartai.setNomorUrut(partai.getNomorUrut());
                    return dataPartai;
                }).orElseThrow(()-> new RuntimeException("Partai not found with id "+partai.getId()));
    }

    @Override
    public void deletePartai(UUID partaiId) {
        partaiRepository.deleteById(partaiId);
    }
}
