package id.co.allobank.exchangerate.runner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import id.co.allobank.exchangerate.dto.BaseResponseDTO;
import id.co.allobank.exchangerate.store.InMemoryStore;
import id.co.allobank.exchangerate.strategy.FinanceDataStrategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataLoaderRunner implements ApplicationRunner {

    private final Map<String, FinanceDataStrategy> strategyMap;
    private final InMemoryStore store;

    public DataLoaderRunner(List<FinanceDataStrategy> strategies,
                            InMemoryStore store) {

        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        FinanceDataStrategy::getType,
                        s -> s
                ));

        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {

        log.info("=====> strategyMap : {}", this.strategyMap);
        Map<String, Object> data = this.strategyMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            try {
                                BaseResponseDTO result = e.getValue().fetch();

                                return result.getData();
                            } catch (Exception ex) {
                                log.error("Error on strategy {}", e.getKey(), ex);
                                return null; // atau fallback
                            }
                        }
                ));

        store.init(data);
    }
}