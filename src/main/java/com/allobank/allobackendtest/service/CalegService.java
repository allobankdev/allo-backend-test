package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.dto.CalegDto;
import com.allobank.allobackendtest.repository.CalegRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalegService {
    private final CalegRepository calegRepository;

    @Autowired
    public CalegService(CalegRepository calegRepository) {
        this.calegRepository = calegRepository;
    }

    public List<CalegDto> getCaleg() {
        List<Caleg> caleg = calegRepository.findAll();
        List<CalegDto> calegDto = caleg.stream().map(CalegDto::from).collect(Collectors.toList());
        return calegDto;
    }

    public List<CalegDto> getCalegByDapil(String namaDapil) {
        List<Caleg> caleg = calegRepository.findByDapil(namaDapil);
        List<CalegDto> calegDto = caleg.stream().map(CalegDto::from).collect(Collectors.toList());
        return calegDto;
    }

    public List<CalegDto> getCalegByPartai(String NamaPartai) {
        List<Caleg> caleg = calegRepository.findByPartai(NamaPartai);
        List<CalegDto> calegDto = caleg.stream().map(CalegDto::from).collect(Collectors.toList());
        return calegDto;
    }
}
