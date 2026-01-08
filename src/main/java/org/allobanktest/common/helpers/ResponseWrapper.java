package org.allobanktest.common.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseWrapper {

    public static <T> BaseResponse<T> success(T data) {
        return success(HttpStatus.OK, "Data fetched successfully", data);
    }

    public static <T> BaseResponse<T> success(HttpStatus status, T data) {
        return success(status, status.getReasonPhrase(), data);
    }

    public static <T> BaseResponse<T> success(HttpStatus status, String message, T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(status.value());
        response.setMessage(message);
        response.setData(data);
        response.setErrors(null);
        response.setServerTime(getServerTime());
        return response;
    }

    public static BaseResponse<Void> error(HttpStatus status, String message) {
        return error(status, message, null);
    }

    public static BaseResponse<Void> error(HttpStatus status, String message, List<String> errors) {
        BaseResponse<Void> response = new BaseResponse<>();
        response.setCode(status.value());
        response.setMessage(message);
        response.setData(null);
        response.setErrors(errors);
        response.setServerTime(getServerTime());
        return response;
    }

    private static String getServerTime() {
        return Instant.now().toString();
    }
}
