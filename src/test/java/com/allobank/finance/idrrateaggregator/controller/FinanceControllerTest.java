package com.allobank.finance.idrrateaggregator.controller;

import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import com.allobank.finance.idrrateaggregator.service.FinanceDataStore;
import com.allobank.finance.idrrateaggregator.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinanceControllerTest {

    @Mock
    private FinanceDataStore store;

    @InjectMocks
    private FinanceController controller;

    @Test
    void shouldReturnFinanceDataForValidResourceType() {
        when(store.get("supported_currencies"))
                .thenReturn(List.of(
                        new FinanceResponse("USD", "United States Dollar")
                ));

        List<FinanceResponse> result =
                controller.getData("supported_currencies");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getKey()).isEqualTo("USD");
    }

    @Test
    void shouldThrowExceptionWhenResourceNotFound() {
        when(store.get("unknown"))
                .thenThrow(new ResourceNotFoundException("Not found"));

        assertThatThrownBy(() ->
                controller.getData("unknown")
        ).isInstanceOf(ResourceNotFoundException.class);
    }
}
