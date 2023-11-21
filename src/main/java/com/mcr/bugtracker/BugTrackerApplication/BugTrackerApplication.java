package com.mcr.bugtracker.BugTrackerApplication;

import com.mcr.bugtracker.BugTrackerApplication.security.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		JwtUtil.class
})
public class BugTrackerApplication {

	public static void main(String[] args) {

		SpringApplication.run(BugTrackerApplication.class, args);
	}

}
