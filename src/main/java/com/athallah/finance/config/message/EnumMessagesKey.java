package com.athallah.finance.config.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumMessagesKey {
    SUCCESS("success.default"),
    DATA_FETCHED_SUCCESS("data.fetched.success"),
    DATA_CREATED_SUCCESS("data.created.success"),
    DATA_UPDATED_SUCCESS("data.updated.success"),
    DATA_DELETED_SUCCESS("data.deleted.success"),

    ERROR_INTERNAL_SERVER_ERROR("error.internal.server"),
    ERROR_BAD_REQUEST("error.bad.request"),
    ERROR_NOT_FOUND("error.not.found"),
    ERROR_UNAUTHORIZED("error.unauthorized"),
    ERROR_FORBIDDEN("error.forbidden"),

    EXT_SERVICE_UNAVAILABLE("external.service.unavailable"),
    EXT_SERVICE_TIMEOUT("external.service.timeout"),
    EXT_SERVICE_BAD_REQUEST("external.service.bad.request"),
    EXT_SERVICE_UNAUTHORIZED("external.service.unauthorized"),
    EXT_SERVICE_FORBIDDEN("external.service.forbidden"),
    EXT_SERVICE_NOT_FOUND("external.service.not.found"),
    EXT_SERVICE_RATE_LIMIT("external.service.rate.limit"),
    EXT_SERVICE_SERVER_ERROR("external.service.server.error"),

    NETWORK_CONNECTION_FAILED("network.connection.failed"),
    NETWORK_TIMEOUT("network.timeout"),
    NETWORK_HOST_UNREACHABLE("network.host.unreachable"),

    INVALID_ARGUMENT("validation.invalid.argument"),
    INVALID_RESOURCE_TYPE("validation.invalid.resource.type"),

    INTERNAL_SERVER_ERROR("error.internal.server"),
    UNEXPECTED_ERROR("error.unexpected");

    private final String messageKey;
}
