package com.allobank.allobackendtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCalegRequest {

    private String dapil;

    private String partai;

    private String sort;
}
