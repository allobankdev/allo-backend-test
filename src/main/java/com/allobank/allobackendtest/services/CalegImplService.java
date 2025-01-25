package com.allobank.allobackendtest.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.repositories.CalegRepository;
import com.allobank.allobackendtest.responses.ResponseMsg;

@Service
public class CalegImplService implements CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Override
    public ResponseMsg<HashMap<String, Object>> SearchCaleg(Integer page, Integer size, String orderBy,
            String orderDirection, String partai, String dapil) {
        ResponseMsg<HashMap<String, Object>> response = new ResponseMsg<>();
        response.setRc("99");
        response.setRm("ERROR");
        try {
            Page<Map<String, Object>> getCaleg;

            Pageable pageable = PageRequest.of(page, size);

            if (orderBy != null && orderDirection != null) {
                switch (orderDirection) {
                    case "asc":
                        pageable = PageRequest.of(page, size, Sort.by(orderBy).ascending());
                        break;
                    case "desc":
                        pageable = PageRequest.of(page, size, Sort.by(orderBy).descending());
                        break;
                    default:
                        break;
                }
            }

            getCaleg = calegRepository.searchCaleg(pageable, partai, dapil);

            HashMap<String, Object> responseData = new HashMap<String, Object>();

            responseData.put("data", getCaleg.getContent());
            responseData.put("currentPage", getCaleg.getNumber());
            responseData.put("totalItems", getCaleg.getTotalElements());
            responseData.put("totalPages", getCaleg.getTotalPages());

            response.setData(responseData);
            response.setRc("00");
            response.setRm("Data Caleg Berhasil Ditampilkan");
        } catch (Exception e) {
            e.printStackTrace();
            response.setRc("99");
            response.setRm(e.getLocalizedMessage());
        }
        return response;
    }

}