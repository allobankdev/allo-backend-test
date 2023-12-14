package com.allobank.allobackendtest.controller.caleg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.allobank.allobackendtest.app.AppResponse;
import com.allobank.allobackendtest.constant.HttpRoute;
import com.allobank.allobackendtest.core.domain.local.caleg.response.CalegResponse;

@RestController
@RequestMapping(HttpRoute.CALEG)
public class CalegController {
    @Autowired
    private CalegViewModel calegViewModel;

    @GetMapping()
    public DeferredResult<ResponseEntity<AppResponse<CalegResponse>>> listCaleg() {
        return calegViewModel.listCaleg();
    }

}