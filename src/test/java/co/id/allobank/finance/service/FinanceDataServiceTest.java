package co.id.allobank.finance.service;

import co.id.allobank.finance.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FinanceDataServiceTest {

    private FinanceDataService service;
    private InMemoryFinanceStore store;

    @BeforeEach
    void init() {
        store = new InMemoryFinanceStore();
        service = new FinanceDataService(store);

        store.put("latest_idr_rates", List.of("dummy"));
    }

    @Test
    void shouldReturnDataIfExists() {
        var result = service.getData("latest_idr_rates");

        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowIfInvalidType() {
        assertThatThrownBy(() -> service.getData("abc"))
                .isInstanceOf(ServiceException.class);
    }
}
