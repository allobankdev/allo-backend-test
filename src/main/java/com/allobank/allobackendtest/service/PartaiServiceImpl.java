package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepo;
import com.allobank.allobackendtest.util.dto.PartaiRequestDTO;
import com.allobank.allobackendtest.util.dto.PartaiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartaiServiceImpl implements PartaiService {

    @Autowired
    private PartaiRepo partaiRepo;

    @Override
    public List<Partai> getAllPartai() {
        return partaiRepo.findAll();
    }

    @Override
    public PartaiResponseDTO createPartai(PartaiRequestDTO partai) {
        Partai p = new Partai();

        p.setNamaPartai(partai.getNamaPartai());
        p.setNomorUrut(partai.getNomorUrut());

        Partai savePartai = partaiRepo.save(p);
        return new PartaiResponseDTO(savePartai);
    }

    @Override
    public PartaiResponseDTO updatePartai(UUID id, PartaiRequestDTO partai) {
        Partai p = partaiRepo.findById(id).orElse(null);

        if (partai.getNamaPartai() != null) {
            assert p != null;
            p.setNamaPartai(partai.getNamaPartai());
        }

        if (partai.getNomorUrut() != null) {
            assert p != null;
            p.setNomorUrut(partai.getNomorUrut());
        }

        assert p != null;
        Partai updatePartai = partaiRepo.save(p);

        return new PartaiResponseDTO(updatePartai);
    }

    @Override
    public void deletePartai(UUID id) {
        partaiRepo.deleteById(id);
    }
}
