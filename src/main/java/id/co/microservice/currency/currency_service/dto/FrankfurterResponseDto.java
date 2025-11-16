package id.co.microservice.currency.currency_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FrankfurterResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String base;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object rates;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String startDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endDate;

}
