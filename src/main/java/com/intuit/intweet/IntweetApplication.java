package com.intuit.intweet;

import com.intuit.intweet.converters.TweetConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCircuitBreaker
public class IntweetApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntweetApplication.class, args);
	}

	@Bean(name = "conversionService")
	ConversionService getConversionService() {
		ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
		bean.setConverters(getConverters());
		bean.afterPropertiesSet();
		return bean.getObject();
	}

	private Set<Converter<?, ?>> getConverters() {
		Set<Converter<?, ?>> converters = new HashSet<>();
		converters.add(new TweetConverter());
		return converters;
	}
}
