package achlaq.co.allo_backend_test.finance.runner;

import achlaq.co.allo_backend_test.finance.strategy.IdrDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FinanceDataInitializer implements ApplicationRunner {

    private final Map<String, IdrDataFetcher> fetchers;

    @Override
    public void run(ApplicationArguments args) {
        fetchers.values().forEach(IdrDataFetcher::load);
    }
}
