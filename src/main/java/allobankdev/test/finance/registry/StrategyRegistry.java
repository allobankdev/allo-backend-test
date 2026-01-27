package allobankdev.test.finance.registry;

import allobankdev.test.finance.strategy.IDRDataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StrategyRegistry {

    private final Map<String, IDRDataFetcher> registry;

    public StrategyRegistry(List<IDRDataFetcher> fetchers) {
        this.registry = fetchers.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::resourceType,
                        Function.identity()
                ));
    }

    public IDRDataFetcher get(String type) {
        IDRDataFetcher fetcher = registry.get(type);
        if (fetcher == null) {
//            throw new InvalidResourceTypeException(type);
        }
        return fetcher;
    }
}
