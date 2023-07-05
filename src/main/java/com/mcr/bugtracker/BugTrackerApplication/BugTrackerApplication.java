package com.mcr.bugtracker.BugTrackerApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties({
//		FileStorageProperties.class
//})
public class BugTrackerApplication {

	public static void main(String[] args) {

		SpringApplication.run(BugTrackerApplication.class, args);
	}

}
