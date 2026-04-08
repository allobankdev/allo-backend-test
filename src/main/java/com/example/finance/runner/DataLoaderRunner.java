package com.example.finance.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.finance.service.IDRDataFetcher;
import com.example.finance.storage.InMemoryDataStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataLoaderRunner {

	private static final Logger log = LoggerFactory.getLogger(DataLoaderRunner.class);

	@Bean
	public ApplicationRunner loadData(List<IDRDataFetcher> fetchers, InMemoryDataStore store) {

		return args -> {

			log.info("Starting data loading at application startup...");

			Map<String, Object> loadedData = new HashMap<>();

			for (IDRDataFetcher fetcher : fetchers) {

				String type = fetcher.getType();

				try {
					Object data = fetcher.fetchData();

					loadedData.put(type, data);

					log.info("Successfully loaded data for type: {}", type);

				} catch (Exception e) {
					log.error("Failed to load data for type: {}", type, e);
				}
			}

			store.setData(loadedData);

			log.info("All data loaded successfully and stored in memory : {}", loadedData);
		};
	}
}