package id.co.evan.project.aggregator.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    GENERAL_ERROR("GNR-999", "Terjadi kesalahan internal pada server"),
    RESOURCE_NOT_FOUND("AGG-001", "Resource tidak tersedia di dalam store"),
    INVALID_INPUT("AGG-002", "Input tidak sesuai dengan ketentuan API");

    private final String code;
    private final String defaultMessage;
}