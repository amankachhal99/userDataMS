package com.aman.userdata.userdatams;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserdatamsApplication {
	private static final Logger log = LogManager.getLogger(UserdatamsApplication.class);

	public static void main(String[] args){
		log.info("Starting the User Data Microservice.");
		SpringApplication.run(UserdatamsApplication.class, args);
	}
}
