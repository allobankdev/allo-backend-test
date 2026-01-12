package id.co.evan.project.aggregator.runner;

import id.co.evan.project.aggregator.config.properties.FinanceProperties;
import id.co.evan.project.aggregator.fault.GeneralException;
import id.co.evan.project.aggregator.service.DataStoreService;
import id.co.evan.project.aggregator.service.strategy.IDRDataFetcher;
import id.co.evan.project.aggregator.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class StartupDataLoader implements ApplicationRunner {

    private final DataStoreService dataStoreService;

    private final FinanceProperties financeProperties;
    private final Map<String, IDRDataFetcher> strategiesByResourceType;

    @Override
    public void run(ApplicationArguments args) {
        var retryCount = financeProperties.retry().count();
        var retryDelay = Duration.ofMillis(financeProperties.retry().delayMs());

        Flux.fromIterable(Constants.RESOURCE_TYPES)
            .flatMap(resourceType ->
                Mono.justOrEmpty(strategiesByResourceType.get(resourceType))
                    .switchIfEmpty(Mono.error(new GeneralException()))
                    .flatMap(strategy -> strategy.fetchData()
                        .retryWhen(Retry.fixedDelay(retryCount, retryDelay))
                        .doOnError(e -> log.error("Fetching Failed {}: {}", resourceType, e.getMessage()))
                        .onErrorResume(e -> Mono.empty())
                    )
            )
            .doOnNext(response -> {
                dataStoreService.putData(response.resourceType(), response);
                log.info("Success Fetch Resource: {}", response.resourceType());
            })
            .collectList()
            .doOnSuccess(ignored -> dataStoreService.finalizeStore())
            .block();
    }
}