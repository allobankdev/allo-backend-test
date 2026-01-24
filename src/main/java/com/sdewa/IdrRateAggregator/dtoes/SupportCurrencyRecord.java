package com.sdewa.IdrRateAggregator.dtoes;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportCurrencyRecord {
   private String currency;
   private String country;
    
}
