package com.bmw.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bmw.store")

public class StoreApplication {

	public static void main(String[] args) {

		SpringApplication.run(StoreApplication.class, args);
	}

}
