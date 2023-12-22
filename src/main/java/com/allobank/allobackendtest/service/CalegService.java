package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dao.calegr;
import com.allobank.allobackendtest.dao.dapilr;
import com.allobank.allobackendtest.dao.partair;
import com.allobank.allobackendtest.entity.calege;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.setvalue.CalegSet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@CalegService
public class CalegService{
    @Autowired
    private calege CalegE;

    public List<Caleg> getCalegs(){
        return calege.findall();
    }
}