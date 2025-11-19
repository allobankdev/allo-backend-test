package com.athallah.finance.config.response_message.localization_messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumMessagesKey {
    SUCCESS("success.default"),
    DATA_FETCHED_SUCCESS("data.fetched.success"),

    ERROR_INTERNAL_SERVER_ERROR("error.internal.server"),
    ERROR_BAD_REQUEST("error.bad.request"),
    ERROR_NOT_FOUND("error.not.found"),
    ERROR_UNAUTHORIZED("error.unauthorized"),
    ERROR_FORBIDDEN("error.forbidden");

    private final String messageKey;
}
