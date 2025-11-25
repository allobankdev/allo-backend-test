package com.example.financedata.dto;

import java.util.Map;

public class HistoricalDto {
    private String from;
    private String to;
    private Map<String, Object> raw;

    // getters & setters
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from;}
    public String getTo(){return to;}
    public void setTo(String to){ this.to = to; }
    public Map<String, Object> getRaw(){ return raw; }
    public void setRaw(Map<String, Object> raw){ this.raw = raw; }
}
