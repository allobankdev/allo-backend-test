package com.allobank.allobackendtest.core.domain.local.caleg.response;

import java.util.List;

import com.allobank.allobackendtest.core.local.caleg.model.Caleg;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CalegResponse {
    private static CalegResponse mInstance = null;

    private CalegResponse() {
    }

    public static synchronized CalegResponse getInstance() {
        if (mInstance == null) {
            mInstance = new CalegResponse();
        }

        return mInstance;
    }

    @JsonProperty("data")
    private List<Caleg> dataList;

    public void setListData(List<Caleg> calegs) {
        this.dataList = calegs;
    }

    public CalegResponse mapToObject(List<Caleg> calegModel) {
        final CalegResponse response = new CalegResponse();
        response.setListData(dataList);
        return response;
    }
}
