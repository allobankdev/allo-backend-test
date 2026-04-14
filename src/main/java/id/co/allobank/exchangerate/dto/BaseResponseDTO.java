package id.co.allobank.exchangerate.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;

import id.co.allobank.exchangerate.common.Constant;
import lombok.Getter;

@Getter
public class BaseResponseDTO<T> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @JsonProperty("responseCode")
    private final String responseCode;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("dateTime")
    private final String dateTime;

    @JsonProperty("data")
    private final T data;

    public BaseResponseDTO(String responseCode, String message, T data) {
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
        this.dateTime = LocalDateTime.now().format(FORMATTER);
    }

    public static <T> BaseResponseDTO<T> success(T data) {
        return new BaseResponseDTO<>(Constant.SUCCESS_CODE, Constant.SUCCESS_MESSAGE, data);
    }

    public static <T> BaseResponseDTO<T> error(String responseCode, String message) {
        return new BaseResponseDTO<>(responseCode, message, null);
    }
}