package com.ampersandor.cotopia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CotopiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CotopiaApplication.class, args);
	}

}
