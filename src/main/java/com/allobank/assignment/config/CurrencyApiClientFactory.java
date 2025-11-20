package com.allobank.assignment.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyApiClientFactory implements FactoryBean<RestTemplate> {

	private final CurrencyApiProperties props;

	public CurrencyApiClientFactory(CurrencyApiProperties props) {
		this.props = props;
	}

	@Override
	public RestTemplate getObject() {

		SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
		rf.setConnectTimeout(props.getConnectTimeoutMs());
		rf.setReadTimeout(props.getReadTimeoutMs());

		RestTemplate rt = new RestTemplate(rf);

		// Ensure JSON parsing works consistently
		rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		// Default headers for safety
		rt.getInterceptors().add((req, body, exec) -> {
			req.getHeaders().set("Accept", "application/json");
			req.getHeaders().set("User-Agent", "IDR-Finance-App/1.0");
			return exec.execute(req, body);
		});

		return rt;
	}

	@Override
	public Class<?> getObjectType() {
		return RestTemplate.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

