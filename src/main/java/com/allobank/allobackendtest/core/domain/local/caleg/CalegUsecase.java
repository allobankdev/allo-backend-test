package com.allobank.allobackendtest.core.domain.local.caleg;

import com.allobank.allobackendtest.core.domain.local.caleg.response.CalegResponse;

import io.reactivex.rxjava3.core.Observable;

public interface CalegUsecase {

    public Observable<CalegResponse> listCaleg(String sort, String dapil, String partai);

}
