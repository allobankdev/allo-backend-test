package com.allobank.finance.model;

import lombok.Data;

import java.util.Map;

/**
 * DTO untuk respons dari endpoint Frankfurter historical (date range).
 * Contoh: /2024-01-01..2024-01-05?from=IDR&to=USD
 */
@Data
public class HistoricalRateResponse {

    /** Jumlah base currency */
    private double amount;

    /** Kode mata uang dasar (IDR) */
    private String base;

    /** Tanggal mulai range */
    private String startDate;

    /** Tanggal akhir range */
    private String endDate;

    /**
     * Map tanggal → (Map simbol → nilai tukar).
     * Contoh: {"2024-01-02": {"USD": 0.000064}}
     */
    private Map<String, Map<String, Double>> rates;
}
