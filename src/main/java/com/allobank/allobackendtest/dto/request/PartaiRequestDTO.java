package com.allobank.allobackendtest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PartaiRequestDTO {
    @NotBlank(message = "Party name is required")
    private String namaPartai;
    @NotBlank(message = "Order number is required")
    private Integer nomorUrut;
}
