package com.allobank.idr_rate_aggregator.service.strategy;

import com.allobank.idr_rate_aggregator.model.ResourceType;

/**
 * Interface Strategy untuk mengambil data IDR dari berbagai resource.
 *
 * Setiap implementasi bertanggung jawab terhadap:
 * - Pemanggilan external API
 * - Transformasi data jika diperlukan
 * - Mengembalikan hasil akhir yang akan disimpan di memory
 *
 * Constraint A:
 * Digunakan untuk menghindari if/else atau switch dalam pemilihan resource.
 */
public interface IDRDataFetcherStrategy {

    /**
     * Mengembalikan tipe resource yang didukung oleh strategy ini.
     */
    ResourceType getSupportedType();

    /**
     * Melakukan pemanggilan API dan mengembalikan hasilnya.
     */
    Object fetchData();
}