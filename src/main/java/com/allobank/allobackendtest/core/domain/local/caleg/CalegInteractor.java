package com.allobank.allobackendtest.core.domain.local.caleg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allobank.allobackendtest.core.local.caleg.CalegService;
import com.allobank.allobackendtest.core.local.caleg.model.Caleg;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Component
public class CalegInteractor implements CalegUsecase {

    @Autowired
    private CalegService calegService;

    @Override
    public Observable<String> listCaleg() {
        return Observable.create(observer -> {
            calegService.listCaleg()
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<List<Caleg>>() {

                        @Override
                        public void onNext(@NonNull List<Caleg> t) {
                            observer.onNext(t.get(0).getNama());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            observer.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            observer.onComplete();
                        }

                    });
        });
    }

}
