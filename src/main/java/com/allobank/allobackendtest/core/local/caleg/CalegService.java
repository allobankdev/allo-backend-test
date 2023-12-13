package com.allobank.allobackendtest.core.local.caleg;

import java.util.List;

import com.allobank.allobackendtest.core.local.caleg.model.Caleg;

import io.reactivex.rxjava3.core.Observable;

public interface CalegService {
    public Observable<List<Caleg>> listCaleg();
}
