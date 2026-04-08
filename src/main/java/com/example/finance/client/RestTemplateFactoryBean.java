package com.example.finance.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("frankfurterRestTemplate")
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

	private RestTemplate restTemplate;

	@Override
	public RestTemplate getObject() {

		if (restTemplate == null) {

			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000);
			factory.setReadTimeout(5000);

			restTemplate = new RestTemplate(factory);
		}

		return restTemplate;
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