package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import java.util.List;

public interface IDRDataFetcher<T> {

    List<T> fetchData();

}
