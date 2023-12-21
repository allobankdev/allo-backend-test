package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.mapper.CalegMapper;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.GetCalegRequest;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private DapilRepository dapilRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    private CalegMapper calegMapper;

    @Transactional
    public List<Caleg> getAllCaleg(GetCalegRequest request) {
        List<CalegEntity> calegList;


        DapilEntity dapilEntity;
        if(Objects.nonNull(request.getDapil())) {
            dapilEntity = dapilRepository.findByNamaDapilIgnoreCase(request.getDapil())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Dapil"));
        }else {
            dapilEntity = null;
        }

        PartaiEntity partaiEntity;
        if(Objects.nonNull(request.getPartai())) {
            partaiEntity = partaiRepository.findByNamaPartaiIgnoreCase(request.getPartai())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Partai "))
            ;
        }else {
            partaiEntity = null;
        }

        if (dapilEntity != null && partaiEntity != null) {
            calegList = calegRepository.findByDapilAndPartai(dapilEntity, partaiEntity);
        } else {
            if (dapilEntity != null) {
                calegList = calegRepository.findByDapil(dapilEntity);
            } else if (partaiEntity != null) {
                calegList = calegRepository.findByPartai(partaiEntity);
            }else {
                calegList = calegRepository.findAll();
            }
        }

        if (calegList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Caleg not found");
        }

        switch (request.getSort()) {
            case "nama":
                calegList.sort(Comparator.comparing(CalegEntity::getNama));
                break;
            default:
                calegList.sort(Comparator.comparingInt(CalegEntity::getNomorUrut));
                break;
        }

        return CalegMapper.toCalegList(calegList);
    }
}
