package com.allobank.allobackendtest.dto;

import lombok.Builder;

@Builder
public record SearchCalegRequest(
        String dapil,
        String partai,
        Integer page,
        Integer size,
        String sortBy

) {
}

