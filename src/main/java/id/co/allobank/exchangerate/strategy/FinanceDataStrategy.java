package id.co.allobank.exchangerate.strategy;

import id.co.allobank.exchangerate.dto.BaseResponseDTO;

public interface FinanceDataStrategy {

    /**
     * Key mapping untuk resourceType dari endpoint
     * contoh:
     * latest_idr_rates
     * historical_idr_usd
     * supported_currencies
     */
    String getType();

    /**
     * Execute logic untuk ambil + transform data
     */
    BaseResponseDTO fetch();
}
