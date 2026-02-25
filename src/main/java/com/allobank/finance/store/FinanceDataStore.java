package com.allobank.finance.store;

import com.allobank.finance.model.FinanceDataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

// Todo : finance data store
@Slf4j
@Component
public class FinanceDataStore {

    private final AtomicReference<Map<String, List<FinanceDataResult>>> storeRef = new AtomicReference<>(
            Collections.emptyMap());

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public void initialize(Map<String, List<FinanceDataResult>> data) {
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException(
                    "FinanceDataStore sudah diinisialisasi. initialize() hanya boleh dipanggil sekali.");
        }

        Map<String, List<FinanceDataResult>> immutableData = data.entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> Collections.unmodifiableList(entry.getValue())));

        storeRef.set(Collections.unmodifiableMap(immutableData));
        log.info("FinanceDataStore berhasil diinisialisasi dengan {} resource types: {}",
                immutableData.size(), immutableData.keySet());
    }

    public Optional<List<FinanceDataResult>> getByResourceType(String resourceType) {
        return Optional.ofNullable(storeRef.get().get(resourceType));
    }

    public boolean isInitialized() {
        return initialized.get();
    }

    public Map<String, List<FinanceDataResult>> getAll() {
        return storeRef.get();
    }
}
