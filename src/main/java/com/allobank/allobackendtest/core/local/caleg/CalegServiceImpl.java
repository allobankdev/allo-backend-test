package com.allobank.allobackendtest.core.local.caleg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.core.local.caleg.model.Caleg;

import io.reactivex.rxjava3.core.Observable;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CalegServiceImpl implements CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Override
    public Observable<List<Caleg>> listCaleg(String sort, String dapil, String partai) {
        return Observable.create(observer -> {
            Sort.Order sortOrder = Sort.Order.by("nomorUrut"); 
            if ("desc".equals(sort)) {
                sortOrder = sortOrder.with(Sort.Direction.DESC);
            } else {
                sortOrder = sortOrder.with(Sort.Direction.ASC);
            }

            List<Caleg> calegList;
            if (dapil != null && partai != null) {
                calegList = calegRepository.findByDapilAndPartai(dapil, partai);
            } else {
                calegList = calegRepository.findAll(Sort.by(sortOrder));
            }

            observer.onNext(calegList);
            observer.onComplete();
        });
    }
}
