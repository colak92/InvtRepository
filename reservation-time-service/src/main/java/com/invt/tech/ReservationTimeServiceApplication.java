package com.invt.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Reservation Time Service Spring Boot application.
 * <p>
 * This class bootstraps the application by launching the Spring context using
 * {@link SpringApplication#run(Class, String...)}.
 */
@SpringBootApplication
public class ReservationTimeServiceApplication {

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments passed during application startup
	 */
	public static void main(String[] args) {
		SpringApplication.run(ReservationTimeServiceApplication.class, args);
	}

}
