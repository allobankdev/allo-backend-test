package com.allobank.dto.response;

import com.allobank.dto.base.BaseCurrencyResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetHistoricalResponse extends BaseCurrencyResponse {
    private LocalDate date;

}
