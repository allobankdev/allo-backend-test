package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.common.CommonResponse;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.payloads.response.CalegResponse;
import com.allobank.allobackendtest.payloads.response.DapilResponse;
import com.allobank.allobackendtest.payloads.response.PartaiResponse;
import com.allobank.allobackendtest.repository.CalegRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CalegService {

    @Autowired
    CalegRepository calegRepository;

    @Transactional
    public CommonResponse<?> getCalegList(String partai, String dapil){
        List<Caleg> calegList = calegRepository.getCalegByPartaiIdAndDApilId(partai, dapil);
        List<CalegResponse> response = new ArrayList<>();

        calegList.forEach(caleg -> {
            response.add(new CalegResponse(
                    caleg.getId(),
                    caleg.getNomorUrut(),
                    caleg.getNama(),
                    caleg.getJenisKelamin(),
                    new DapilResponse(caleg.getDapil().getNamaDapil(), caleg.getDapil().getProvinsi(), caleg.getDapil().getWilayahDapilList(), caleg.getDapil().getJumlahKursi()),
                    new PartaiResponse(caleg.getPartai().getNamaPartai(), caleg.getPartai().getNomorUrut())
            ));
        });

        return new CommonResponse(0, "get all list caleg", response);
    }
}
