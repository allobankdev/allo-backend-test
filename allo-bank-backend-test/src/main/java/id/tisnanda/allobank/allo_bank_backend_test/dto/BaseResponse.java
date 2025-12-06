package id.tisnanda.allobank.allo_bank_backend_test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse <T> {

    private String code ;

    private String message ;

    private String resource ;

    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>("200", "Success", "ALBS", data);
    }

}
