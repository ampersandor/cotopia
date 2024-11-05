package com.ampersandor.leettrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LeettrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeettrackApplication.class, args);
	}

}
