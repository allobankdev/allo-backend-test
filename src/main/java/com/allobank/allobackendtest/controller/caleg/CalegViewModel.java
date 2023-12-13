package com.allobank.allobackendtest.controller.caleg;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.allobank.allobackendtest.core.domain.local.caleg.CalegUsecase;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Component
public class CalegViewModel {
    @Autowired
    private CalegUsecase calegUsecase;

    public DeferredResult<ResponseEntity<String>> listCaleg() {
        final DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
        calegUsecase.listCaleg()
        .subscribeOn(Schedulers.io())
        .subscribeWith(new DisposableObserver<String>() {

            @Override
            public void onNext(@NonNull String t) {
                result.setResult(ResponseEntity.status(200).body(t));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                result.setErrorResult(ResponseEntity.status(500).body(e.getMessage()));
            }

            @Override
            public void onComplete() {
            }

        });
        return result;
    }
}
