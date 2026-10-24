package com.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")

public class TransactionManagerApplication {

	public static void main(String[] args) {
		// Must be set before Spring/Hibernate start: anything initialised earlier (e.g. Hibernate's
		// @CreationTimestamp clock) otherwise keeps the container's UTC zone.
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(TransactionManagerApplication.class, args);
	}

}
