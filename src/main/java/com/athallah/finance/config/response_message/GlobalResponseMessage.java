package com.athallah.finance.config.response_message;

import com.athallah.finance.config.response_message.localization_messages.LocalizedMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class GlobalResponseMessage {

    private String code;
    private Message message;
    private Object data;

    @Builder(builderMethodName = "builder")
    public GlobalResponseMessage(String code, Message message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // SUCCESS
    public void buildSuccessResponse(String messageKey, String... args) {
        this.code = String.valueOf(HttpStatus.OK.value());
        this.message = new Message(messageKey, args);
    }

    // ERROR
    public void buildErrorResponse(HttpStatus status, String messageKey, String... args) {
        this.code = String.valueOf(status.value());
        this.message = new Message(messageKey, args);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String id;  // Indonesian
        private String en;  // English

        public Message(String messageKey, String... args) {
            this.id = LocalizedMessages.getLocalizedMessageIndonesian(messageKey, args);
            this.en = LocalizedMessages.getLocalizedMessageEnglish(messageKey, args);
        }
    }
}
