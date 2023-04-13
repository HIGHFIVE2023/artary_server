package com.highfive.artary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ArtaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtaryApplication.class, args);
	}

}
