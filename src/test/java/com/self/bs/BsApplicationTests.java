package com.self.bs;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import com.self.bs.source.config.ExchangeRateProperties;
import com.self.bs.source.enumeration.CacheKeywordEnum;

@SpringBootTest
class BsApplicationTests {
	@Autowired
    protected ConcurrentMapCacheManager cacheManager;

	@Autowired
	protected ExchangeRateProperties exchangeRateProperties;

	@Test
	void contextLoads() {
		String dateFrom = LocalDate.now().minusDays(exchangeRateProperties.getDefaultHistoricalRangeDate()).format(DateTimeFormatter.ofPattern(exchangeRateProperties.getDateFormat()));
        String dateTo = LocalDate.now().format(DateTimeFormatter.ofPattern(exchangeRateProperties.getDateFormat()));

		String rangeDate = dateFrom.concat(exchangeRateProperties.getRangeDateSeparator()).concat(dateTo);

		List<String> cacheNameList = List.of(CacheKeywordEnum.CURRENCY_LIST.name(), 
				CacheKeywordEnum.HISTORICAL.name().concat(rangeDate), 
				CacheKeywordEnum.LATEST_RATES.name());

		for (String key : cacheNameList){
			assertNotNull(cacheManager.getCache(exchangeRateProperties.getCacheName()).get(key));
		}
	}

}
