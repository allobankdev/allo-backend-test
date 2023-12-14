package com.allobank.allobackendtest.core.local.caleg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Observable<List<Caleg>> listCaleg() {
        return Observable.create(observer -> {
            observer.onNext(calegRepository.findAll());
            observer.onComplete();

            return;
        });
    }


}
