package com.mariano.spacecrafts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories
@EnableCaching
public class SpacecraftsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpacecraftsApplication.class, args);
	}
}