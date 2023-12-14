package com.allobank.allobackendtest.controller.caleg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.allobank.allobackendtest.app.AppResponse;
import com.allobank.allobackendtest.core.domain.local.caleg.CalegUsecase;
import com.allobank.allobackendtest.core.domain.local.caleg.response.CalegResponse;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Component
public class CalegViewModel {
    @Autowired
    private CalegUsecase calegUsecase;

    public DeferredResult<ResponseEntity<AppResponse<CalegResponse>>> listCaleg() {
        final AppResponse<CalegResponse> response = new AppResponse<>();
        final DeferredResult<ResponseEntity<AppResponse<CalegResponse>>> result = new DeferredResult<>();
        calegUsecase.listCaleg()
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<CalegResponse>() {

                    @Override
                    public void onNext(@NonNull CalegResponse calegResponse) {
                        response.setCode(200);
                        response.setData(calegResponse);
                        response.setMessage("Success");
                        response.setErrorMessage("");
                        result.setResult(ResponseEntity.ok().body(response));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        response.setCode(e.hashCode());
                        response.setErrorMessage(e.getMessage());
                        response.setMessage("Failed get list caleg.");
                        result.setErrorResult(ResponseEntity.internalServerError().body(response));
                    }

                    @Override
                    public void onComplete() {
                    }

                });
        return result;
    }
}
