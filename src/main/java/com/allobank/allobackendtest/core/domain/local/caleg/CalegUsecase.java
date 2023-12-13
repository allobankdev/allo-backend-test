package com.allobank.allobackendtest.core.domain.local.caleg;

import io.reactivex.rxjava3.core.Observable;

public interface CalegUsecase {

    public Observable<String> listCaleg();

}
