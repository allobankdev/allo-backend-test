package id.tisnanda.allobank.allo_bank_backend_test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private final String error;
    private final String path;
    private final String timestamp;

}
