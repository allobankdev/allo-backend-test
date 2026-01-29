package com.allobank.financeaggregator.config;

import java.time.LocalDate;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "finance.historical")
public class HistoricalProperties {

    private String range;
    private LocalDate startDate;
    private LocalDate endDate;
    private String from;
    private String to;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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

    public String resolveRangeString() {
        if (startDate != null && endDate != null) {
            return startDate + ".." + endDate;
        }
        return range;
    }

    public Range resolveRange() {
        if (startDate != null && endDate != null) {
            return new Range(startDate, endDate);
        }
        if (range == null || !range.contains("..")) {
            return null;
        }
        String[] parts = range.split("\\.\\.");
        if (parts.length != 2) {
            return null;
        }
        try {
            LocalDate start = LocalDate.parse(parts[0]);
            LocalDate end = LocalDate.parse(parts[1]);
            return new Range(start, end);
        } catch (Exception ex) {
            return null;
        }
    }

    public record Range(LocalDate start, LocalDate end) {
    }
}
