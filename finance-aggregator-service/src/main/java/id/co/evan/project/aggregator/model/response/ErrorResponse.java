package id.co.evan.project.aggregator.model.response;

import id.co.evan.project.aggregator.model.BaseResponse;

public record ErrorResponse(
    String errorCode,
    String errorMessage,
    String timestamp,
    String path
) implements BaseResponse { }