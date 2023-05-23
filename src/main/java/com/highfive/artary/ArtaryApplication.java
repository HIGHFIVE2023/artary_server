package com.highfive.artary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

@SpringBootApplication
@EnableJpaAuditing
public class ArtaryApplication {

	CommonOAuth2Provider commonOAuth2Provider;

	public static void main(String[] args) {
		SpringApplication.run(ArtaryApplication.class, args);
	}

}
