package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.exception.DataInvalidException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class SplitStrategyFactory {

    private final Map<SplitTypeEnum, SplitStrategy> strategies;

    public SplitStrategyFactory(EqualSplitStrategy equal,
                                QuantitySplitStrategy quantity,
                                ExactSplitStrategy exact,
                                PercentageSplitStrategy percentage) {

        strategies = Map.of(
                SplitTypeEnum.EQUAL, equal,
                SplitTypeEnum.QUANTITY, quantity,
                SplitTypeEnum.EXACT, exact,
                SplitTypeEnum.PERCENTAGE, percentage
        );
    }

    public SplitStrategy get(SplitTypeEnum type) {
        SplitStrategy strategy = strategies.get(type);
        if (Objects.isNull(strategy)) {
            throw new DataInvalidException("Unsupported split type: " + type);
        }
        return strategy;
    }
}
