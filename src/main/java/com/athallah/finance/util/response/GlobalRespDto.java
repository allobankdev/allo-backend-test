package com.athallah.finance.util.response;


import com.athallah.finance.config.response_message.GlobalResponseMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GlobalRespDto extends GlobalResponseMessage {

    public GlobalRespDto(Object data, String messageKey, String... messageArgs) {
        super.buildSuccessResponse(messageKey, messageArgs);
        setData(data);
    }

    public static GlobalRespDtoBuilder successResponseBuilder() {
        return new GlobalRespDtoBuilder();
    }

    public static class GlobalRespDtoBuilder {
        private Object data;
        private String messageKey;
        private String[] messageArgs;

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

        public GlobalRespDto build() {
            return new GlobalRespDto(data, messageKey, messageArgs);
        }
    }
}


