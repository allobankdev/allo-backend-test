package com.athallah.finance.util.response;


import com.athallah.finance.config.message.GlobalResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL) // Hanya tampilkan field yang tidak null
public class GlobalRespDto extends GlobalResponseMessage {

    private String errorCode;      // Kode error untuk client
    private String errorDetails;   // Detail error untuk debugging
    private LocalDateTime timestamp; // Waktu terjadinya error

    // Constructor untuk SUCCESS response
    public GlobalRespDto(Object data, String messageKey, String... messageArgs) {
        super.buildSuccessResponse(messageKey, messageArgs);
        setData(data);
        this.timestamp = LocalDateTime.now();
    }

    // Constructor untuk ERROR response
    private GlobalRespDto(String messageKey, String errorCode, String errorDetails, String... messageArgs) {
        super.buildErrorResponse(messageKey, messageArgs);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
        this.timestamp = LocalDateTime.now();
    }

    // Builder untuk SUCCESS response
    public static GlobalRespDtoBuilder successResponseBuilder() {
        return new GlobalRespDtoBuilder(true);
    }

    // Builder untuk ERROR response
    public static GlobalRespDtoBuilder errorResponseBuilder() {
        return new GlobalRespDtoBuilder(false);
    }

    public static class GlobalRespDtoBuilder {
        private final boolean isSuccess;
        private Object data;
        private String messageKey;
        private String[] messageArgs;
        private String errorCode;
        private String errorDetails;

        private GlobalRespDtoBuilder(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public GlobalRespDtoBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public GlobalRespDtoBuilder message(String key) {
            this.messageKey = key;
            return this;
        }

        public GlobalRespDtoBuilder messageData(String... args) {
            this.messageArgs = args;
            return this;
        }

        public GlobalRespDtoBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public GlobalRespDtoBuilder errorDetails(String errorDetails) {
            this.errorDetails = errorDetails;
            return this;
        }

        public GlobalRespDto build() {
            if (isSuccess) {
                return new GlobalRespDto(data, messageKey, messageArgs);
            } else {
                return new GlobalRespDto(messageKey, errorCode, errorDetails, messageArgs);
            }
        }
    }
}

