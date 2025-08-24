package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.request.CalegRequestDTO;
import com.allobank.allobackendtest.dto.response.CalegResponseDTO;
import com.allobank.allobackendtest.dto.response.DapilResponseDTO;
import com.allobank.allobackendtest.dto.response.PartaiResponseDTO;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
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
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;
    @Autowired
    private DapilRepository dapilRepository;
    @Autowired
    private PartaiRepository partaiRepository;

    public Page<CalegResponseDTO> findAll(String namaPartai, String namaDapil, int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Caleg> calegPage;

        if (namaPartai != null && namaDapil != null) {
            calegPage = calegRepository.findByPartaiNamaPartaiAndDapilNamaDapil(namaPartai, namaDapil, pageable);
        } else if (namaPartai != null) {
            calegPage = calegRepository.findByPartaiNamaPartai(namaPartai, pageable);
        } else if (namaDapil != null) {
            calegPage = calegRepository.findByDapilNamaDapil(namaDapil, pageable);
        } else {
            calegPage = calegRepository.findAll(pageable);
        }

        Page<CalegResponseDTO> calegResponseDTOPage = calegPage.map(c -> {
            CalegResponseDTO calegResponseDTO = new CalegResponseDTO();
            calegResponseDTO.setNama(c.getNama());
            calegResponseDTO.setNomorUrut(c.getNomorUrut());
            calegResponseDTO.setJenisKelamin(c.getJenisKelamin());

            PartaiResponseDTO  partaiResponseDTO = new PartaiResponseDTO();
            partaiResponseDTO.setNamaPartai(c.getPartai().getNamaPartai());
            partaiResponseDTO.setNomorUrut(c.getPartai().getNomorUrut());
            calegResponseDTO.setPartai(partaiResponseDTO);

            DapilResponseDTO dapilResponseDTO = new DapilResponseDTO();
            dapilResponseDTO.setNamaDapil(c.getDapil().getNamaDapil());
            dapilResponseDTO.setProvinsi(c.getDapil().getProvinsi());
            dapilResponseDTO.setJumlahKursi(c.getDapil().getJumlahKursi());
            dapilResponseDTO.setWilayahDapilList(c.getDapil().getWilayahDapilList());
            calegResponseDTO.setDapil(dapilResponseDTO);

            return calegResponseDTO;
        });

        return calegResponseDTOPage;
    }

    public Optional<CalegResponseDTO> findById(UUID id) {
        Optional<Caleg> caleg = calegRepository.findById(id);

        if (caleg.isPresent()) {
            Optional<CalegResponseDTO> calegResponseDTO = caleg.map(c -> {
                CalegResponseDTO dto = new CalegResponseDTO();
                dto.setNama(c.getNama());
                dto.setNomorUrut(c.getNomorUrut());
                dto.setJenisKelamin(c.getJenisKelamin());

                PartaiResponseDTO  partaiResponseDTO = new PartaiResponseDTO();
                partaiResponseDTO.setNamaPartai(c.getPartai().getNamaPartai());
                partaiResponseDTO.setNomorUrut(c.getPartai().getNomorUrut());
                dto.setPartai(partaiResponseDTO);

                DapilResponseDTO dapilResponseDTO = new DapilResponseDTO();
                dapilResponseDTO.setNamaDapil(c.getDapil().getNamaDapil());
                dapilResponseDTO.setProvinsi(c.getDapil().getProvinsi());
                dapilResponseDTO.setJumlahKursi(c.getDapil().getJumlahKursi());
                dapilResponseDTO.setWilayahDapilList(c.getDapil().getWilayahDapilList());
                dto.setDapil(dapilResponseDTO);

                return dto;
            });
            return calegResponseDTO;
        }

        return Optional.empty();
    }

    public CalegResponseDTO save(CalegRequestDTO calegRequestDTO) {
        Partai partai = partaiRepository.findByNamaPartai(calegRequestDTO.getNamaPartai())
                .orElseThrow(() -> new EntityNotFoundException("Partai not found"));

        Dapil dapil = dapilRepository.findByNamaDapil(calegRequestDTO.getNamaDapil())
                .orElseThrow(() -> new EntityNotFoundException("Dapil not found"));

        if (calegRepository.existsByNama(calegRequestDTO.getNama())) {
            throw new EntityExistsException("Caleg name already exists");
        }

        Caleg caleg = new Caleg();
        caleg.setNama(calegRequestDTO.getNama());
        caleg.setNomorUrut(calegRequestDTO.getNomorUrut());
        caleg.setJenisKelamin(calegRequestDTO.getJenisKelamin());
        caleg.setPartai(partai);
        caleg.setDapil(dapil);

        Caleg saved = calegRepository.save(caleg);

        CalegResponseDTO calegResponseDTO = new CalegResponseDTO();
        calegResponseDTO.setNama(saved.getNama());
        calegResponseDTO.setNomorUrut(saved.getNomorUrut());
        calegResponseDTO.setJenisKelamin(saved.getJenisKelamin());

        PartaiResponseDTO partaiDTO = new PartaiResponseDTO();
        partaiDTO.setNamaPartai(partai.getNamaPartai());
        partaiDTO.setNomorUrut(partai.getNomorUrut());
        calegResponseDTO.setPartai(partaiDTO);

        DapilResponseDTO dapilDTO = new DapilResponseDTO();
        dapilDTO.setNamaDapil(dapil.getNamaDapil());
        dapilDTO.setProvinsi(dapil.getProvinsi());
        dapilDTO.setJumlahKursi(dapil.getJumlahKursi());
        calegResponseDTO.setDapil(dapilDTO);

        return calegResponseDTO;
    }

    public CalegResponseDTO update(CalegRequestDTO calegRequestDTO, UUID id) {
        Caleg caleg = calegRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Caleg not found"));

        Partai partai = partaiRepository.findByNamaPartai(calegRequestDTO.getNamaPartai())
                .orElseThrow(() -> new EntityNotFoundException("Partai not found"));

        Dapil dapil = dapilRepository.findByNamaDapil(calegRequestDTO.getNamaDapil())
                .orElseThrow(() -> new EntityNotFoundException("Dapil not found"));

        if (calegRepository.existsByNama(calegRequestDTO.getNama()) &&
                !caleg.getNama().equals(calegRequestDTO.getNama())) {
            throw new EntityExistsException("Caleg name already exists");
        }

        caleg.setNama(calegRequestDTO.getNama());
        caleg.setNomorUrut(calegRequestDTO.getNomorUrut());
        caleg.setJenisKelamin(calegRequestDTO.getJenisKelamin());
        caleg.setPartai(partai);
        caleg.setDapil(dapil);

        Caleg savedCaleg = calegRepository.save(caleg);

        CalegResponseDTO calegResponseDTO = new CalegResponseDTO();
        calegResponseDTO.setNama(savedCaleg.getNama());
        calegResponseDTO.setNomorUrut(savedCaleg.getNomorUrut());
        calegResponseDTO.setJenisKelamin(savedCaleg.getJenisKelamin());

        PartaiResponseDTO partaiResponseDTO = new PartaiResponseDTO();
        partaiResponseDTO.setNamaPartai(partai.getNamaPartai());
        partaiResponseDTO.setNomorUrut(partai.getNomorUrut());
        calegResponseDTO.setPartai(partaiResponseDTO);

        DapilResponseDTO dapilResponseDTO = new DapilResponseDTO();
        dapilResponseDTO.setNamaDapil(dapil.getNamaDapil());
        dapilResponseDTO.setProvinsi(dapil.getProvinsi());
        dapilResponseDTO.setWilayahDapilList(dapil.getWilayahDapilList());
        dapilResponseDTO.setJumlahKursi(dapil.getJumlahKursi());
        calegResponseDTO.setDapil(dapilResponseDTO);

        return calegResponseDTO;
    }

    public void deleteById(UUID id) {
        calegRepository.deleteById(id);
    }
}
