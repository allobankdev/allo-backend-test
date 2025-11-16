package id.co.microservice.currency.currency_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CurrencyResponseDto extends FrankfurterResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object currencies;

}
