package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.dto.CalegDTO;
import com.allobank.allobackendtest.dto.DapilDTO;
import com.allobank.allobackendtest.dto.PartaiDTO;
import com.allobank.allobackendtest.model.Caleg;
import org.springframework.stereotype.Component;

@Component
public class CalegMapper {

    public CalegDTO toDTO(Caleg caleg) {
        if (caleg == null) return null;

        DapilDTO dapilDTO = null;
        if (caleg.getDapil() != null) {
            dapilDTO = DapilDTO.builder()
                    .id(caleg.getDapil().getId())
                    .namaDapil(caleg.getDapil().getNamaDapil())
                    .provinsi(caleg.getDapil().getProvinsi())
                    .build();
        }

        PartaiDTO partaiDTO = null;
        if (caleg.getPartai() != null) {
            partaiDTO = PartaiDTO.builder()
                    .id(caleg.getPartai().getId())
                    .namaPartai(caleg.getPartai().getNamaPartai())
                    .nomorUrut(caleg.getPartai().getNomorUrut())
                    .build();
        }

        return CalegDTO.builder()
                .id(caleg.getId())
                .nomorUrut(caleg.getNomorUrut())
                .nama(caleg.getNama())
                .jenisKelamin(caleg.getJenisKelamin())
                .dapil(dapilDTO)
                .partai(partaiDTO)
                .build();
    }
}