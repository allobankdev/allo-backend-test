package com.allobank.allobackendtest.strategy;

public interface IdrDataFetcher {

    /** Nama resourceType */
    String resourceType();

    /** Dipanggil sekali saat startup untuk fetch yang berasal dari API eksternal */
    Object fetchFromApi();
}
