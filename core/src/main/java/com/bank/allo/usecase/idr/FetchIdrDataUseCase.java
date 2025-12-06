package com.bank.allo.usecase.idr;

import com.bank.allo.exception.BadRequestException;
import com.bank.allo.usecase.UseCase;
import lombok.Builder;
import lombok.Value;
import java.util.Map;

public class FetchIdrDataUseCase
        extends UseCase<FetchIdrDataUseCase.InputValues, FetchIdrDataUseCase.OutputValues> {

    private final Map<String, IdrDataFetcher> fetcherRegistry;

    public FetchIdrDataUseCase(Map<String, IdrDataFetcher> fetcherRegistry) {
        this.fetcherRegistry = fetcherRegistry;
    }

    @Override
    public OutputValues execute(InputValues input) {

        IdrDataFetcher fetcher = fetcherRegistry.get(input.getResourceType());

        if (fetcher == null) {
            throw new BadRequestException("Unknown resource type: " + input.getResourceType());
        }

        Object result = fetcher.fetch();

        return OutputValues.builder()
                .result(result)
                .build();
    }

    @Value @Builder(builderClassName = "Builder")
    public static class InputValues implements UseCase.InputValues {
        String resourceType;
    }

    @Value @Builder(builderClassName = "Builder")
    public static class OutputValues implements UseCase.OutputValues {
        Object result;
    }
}
