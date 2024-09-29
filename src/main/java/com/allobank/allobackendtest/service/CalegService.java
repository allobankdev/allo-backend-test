package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dao.CalegDao;
import com.allobank.allobackendtest.dto.CalegResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalegService {

    @Autowired
    private CalegDao calegDao;

    public List<CalegResponseDto> getListCaleg(String dapil, String partai) {
        return calegDao.getListCaleg(dapil, partai);
    }
}
