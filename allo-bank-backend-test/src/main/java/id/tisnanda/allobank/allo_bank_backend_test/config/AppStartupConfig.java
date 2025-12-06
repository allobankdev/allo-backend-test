package id.tisnanda.allobank.allo_bank_backend_test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class AppStartupConfig {
    @Getter
    @Setter
    private boolean startupMode = true;
}
