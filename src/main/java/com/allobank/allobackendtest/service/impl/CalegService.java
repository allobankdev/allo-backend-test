package com.allobank.allobackendtest.service.impl;

import com.allobank.allobackendtest.dto.CalegResponse;
import com.allobank.allobackendtest.dto.Response;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.service.CalegServiceInterface;
import com.allobank.allobackendtest.exception.GlobalException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CalegService implements CalegServiceInterface {

    @Autowired
    private CalegRepository calegRepos;

    @Override
    public Response listAllCaleg(String dapilFilter, String partaiFilter, String sortBy) {
        log.info("[Start] - listAllCaleg");
        log.info("listAllCaleg: dapilFilter-" + dapilFilter + " | partaiFilter-" + partaiFilter + " | sortBy-" + sortBy);
        List<CalegResponse> calegResponse = new ArrayList<CalegResponse>();
        List<Caleg> calegList = calegRepos.findAll();

//       FILTER DAPIL DAN PARTAI
        if (dapilFilter != null || partaiFilter != null) {
            calegList = calegList.stream()
                    .filter(caleg -> (dapilFilter == null || caleg.getDapil().getNamaDapil().equalsIgnoreCase(dapilFilter)) &&
                            (partaiFilter == null || caleg.getPartai().getNamaPartai().equalsIgnoreCase(partaiFilter)))
                    .collect(Collectors.toList());
            log.info("calegList: " + calegList);

            if (calegList.isEmpty()){
                log.info("listAllCaleg: Data Tidak Ditemukan");
                throw new GlobalException("002", HttpStatus.BAD_GATEWAY);
            }
        }

//       SORTING NOMOR URUT
        if ("nomorUrut".equals(sortBy)) {
            log.info("listAllCaleg: Filter By nomorUrut");
            calegList.sort((c1, c2) -> c1.getNomorUrut().compareTo(c2.getNomorUrut()));
        } else if ("nama".equals(sortBy)) {
            log.info("listAllCaleg: Filter By nama");
            calegList.sort((c1, c2) -> c1.getNama().compareToIgnoreCase(c2.getNama()));
        } else {
            log.info("listAllCaleg: Filter Value Tidak Sesuai");
            throw new GlobalException("001", HttpStatus.BAD_GATEWAY);
        }

        for (Caleg caleg : calegList) {
            CalegResponse calegResp = CalegResponse.builder()
                    .id(caleg.getId())
                    .dapil(caleg.getDapil())
                    .partai(caleg.getPartai())
                    .nomorUrut(caleg.getNomorUrut())
                    .nama(caleg.getNama())
                    .jenisKelamin(caleg.getJenisKelamin())
                    .build();
            calegResponse.add(calegResp);
        }

        Response response = new Response();
        response.setRc("000");
        response.setMessage("Success");
        response.setData(calegResponse);

        log.info("[END] - listAllCaleg");

        return response;
    }
}
