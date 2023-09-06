package com.zuucker.hobbyhubbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "controllers")
public class HobbyHubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HobbyHubBackendApplication.class, args);
	}

}
