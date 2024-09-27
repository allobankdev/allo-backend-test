package com.allobank.allobackendtest.services;

import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CalegService {
    @Autowired
    CalegRepository calegRepository;

    @Autowired
    DapilRepository dapilRepository;

    @Autowired
    PartaiRepository partaiRepository;

    public boolean addCalegSvc(CalegDto calegDto) {
        Caleg caleg = new Caleg();
        Dapil dapil = new Dapil();
        Partai partai = new Partai();
        boolean result = false;

        try {

            caleg.setNamaCaleg(calegDto.getNamaCaleg());
            caleg.setNomorUrut(calegDto.getNomorUrut());

            dapil = dapilRepository.findById(calegDto.getDapil().getId()).orElse(new Dapil());
            partai = partaiRepository.findById(calegDto.getPartai().getId()).orElse(new Partai());
            if (dapil.getId() != 0 && partai.getId() != 0) {
                caleg.setDapil(dapil);
                caleg.setPartai(partai);
                caleg.setCreatedAt(new Date());
                result = true;
            } else {
                result= false;
               throw new Exception("dapil or partai not found! ");
            }

            calegRepository.save(caleg);

        } catch (Exception e) {
            System.out.println("addCalegSvc");
            e.printStackTrace();
        }
        return result;
    }

    public Page<CalegDto> getListCalegByDapilNameandPartaiName(String dapilName, String partaiName, int pageNumber, int pageSize) {
        List<Caleg> listCaleg = new ArrayList<>();
        Page pageable = null;
        Sort sort = Sort.by(Sort.Direction.ASC, "nomorUrut");

        try {
            pageable = calegRepository.findByDapilNamaDapilContainingAndPartaiNamaPartaiContaining
                    (dapilName, partaiName, PageRequest.of(pageNumber, pageSize, sort)).map(this::convertToDto);

        } catch (Exception e) {
            System.out.println("getListCalegByDapilNameandPartaiName");
            e.printStackTrace();
        }

        return pageable;
    }

    private CalegDto convertToDto(Caleg caleg) {
        CalegDto calegDto = new CalegDto();
        calegDto.setNamaCaleg(caleg.getNamaCaleg());
        calegDto.setNomorUrut(caleg.getNomorUrut());
        calegDto.setPartai(caleg.getPartai());
        calegDto.setDapil(caleg.getDapil());
        calegDto.setCreatedAt(caleg.getCreatedAt());
        return calegDto;
    }

}