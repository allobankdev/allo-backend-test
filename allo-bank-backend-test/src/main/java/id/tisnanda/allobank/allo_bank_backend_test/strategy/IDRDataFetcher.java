package id.tisnanda.allobank.allo_bank_backend_test.strategy;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public interface IDRDataFetcher<T> {

    List<T> fetchData();

}
