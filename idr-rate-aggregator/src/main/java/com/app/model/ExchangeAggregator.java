package com.app.model;

import javax.persistence.*;

@Entity
@Table(name = "exchange_aggregator")
public class ExchangeAggregator {

    @Id
    private String id;

    @Lob
    private String data;

    private String isErr;

    private String errMessage;

    private Integer errCode;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIsErr() {
        return isErr;
    }

    public void setIsErr(String isErr) {
        this.isErr = isErr;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }
}
