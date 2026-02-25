package com.allobank.finance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * DTO untuk respons dari endpoint Frankfurter /latest?base=IDR.
 * Digunakan oleh
 * {@link com.allobank.finance.strategy.impl.LatestIdrRatesFetcher}.
 */
@Data
public class LatestRateResponse {

    /** Jumlah base currency (selalu 1.0 untuk IDR) */
    private double amount;

    /** Kode mata uang dasar (IDR) */
    private String base;

    /** Tanggal rate berlaku */
    private String date;

    /** Map simbol mata uang → nilai tukar terhadap IDR */
    private Map<String, Double> rates;

    /**
     * Field tambahan hasil kalkulasi business logic:
     * Harga jual USD dalam IDR setelah spread perbankan.
     * Tidak berasal dari API, dihitung di aplikasi.
     */
    @JsonProperty("USD_BuySpread_IDR")
    private Double usdBuySpreadIdr;
}
