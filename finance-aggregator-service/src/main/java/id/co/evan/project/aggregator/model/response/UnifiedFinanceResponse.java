package id.co.evan.project.aggregator.model.response;

import id.co.evan.project.aggregator.model.BaseResponse;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

@RecordBuilder
public record UnifiedFinanceResponse(
    @NotBlank
    String resourceType,
    @NotNull
    String fetchDate,
    List<Map<String, Object>> data
) implements BaseResponse { }