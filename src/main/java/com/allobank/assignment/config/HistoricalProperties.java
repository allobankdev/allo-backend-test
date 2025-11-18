package com.allobank.assignment.config;

import org.springframework.util.Assert;

import java.time.LocalDate;

public class HistoricalProperties {
    private LocalDate startDate = LocalDate.of(2024, 1, 1);
    private LocalDate endDate = LocalDate.of(2024, 1, 5);
    private String from = "IDR";
    private String to ="USD";

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        Assert.notNull(startDate, "Start date must not be null");
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        Assert.notNull(endDate, "End date must not be null");
        this.endDate = endDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
