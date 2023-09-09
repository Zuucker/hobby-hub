package com.zuucker.hobbyhubbackend;

import database.DatabaseManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "controllers")
public class HobbyHubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HobbyHubBackendApplication.class, args);
                
                //close database connection when program exits
                Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    DatabaseManager manager = DatabaseManager.getInstance();
                    manager.close();
                }
            });
	}

}
