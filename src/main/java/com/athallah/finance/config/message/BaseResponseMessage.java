package com.athallah.finance.config.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseMessage {

    private ErrorSchema errorSchema = new ErrorSchema();

    @Builder(builderMethodName = "baseResponseBuilder")
    public BaseResponseMessage(String errorCode, ErrorMessage errorMessage) {
        this.errorSchema.errorCode = errorCode;
        this.errorSchema.errorMessage = errorMessage;
    }

    public void buildSuccessResponse() {
        errorSchema = ErrorSchema.builder()
                .errorCode(String.valueOf(HttpStatus.OK.value()))
                .errorMessage(new ErrorMessage(EnumMessagesKey.SUCCESS.getMessageKey()))
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorSchema {
        private String errorCode;
        private ErrorMessage errorMessage;
    }

    @Data
    @NoArgsConstructor
    public static class ErrorMessage {
        private String indonesian;
        private String english;

        public ErrorMessage(String messageKey) {
            this.indonesian = LocalizedMessages.getLocalizedMessageIndonesian(messageKey);
            this.english = LocalizedMessages.getLocalizedMessageEnglish(messageKey);
        }

        public ErrorMessage(String messageKey, String...args) {
            this.indonesian = LocalizedMessages.getLocalizedMessageIndonesian(messageKey, args);
            this.english = LocalizedMessages.getLocalizedMessageEnglish(messageKey, args);
        }
    }
}