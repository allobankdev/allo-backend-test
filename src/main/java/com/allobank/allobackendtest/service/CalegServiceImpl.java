package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.exception.CalegNotFoundException;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CalegServiceImpl implements CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Override
    public List<Caleg> getAllCalegs(String namaDapil, String namaPartai, Integer page, Integer size, String sortBy) throws CalegNotFoundException {
        Sort sort = Sort.by(Sort.Order.asc(sortBy != null ? sortBy : "nomorUrut"));
        PageRequest pageRequest = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);

        List<Caleg> calegs = calegRepository.findByFilters(namaDapil, namaPartai, pageRequest).getContent();

        if (calegs.isEmpty()) {
            throw new CalegNotFoundException("No Calegs found for the given filters");
        }

        return calegs;
    }

}
