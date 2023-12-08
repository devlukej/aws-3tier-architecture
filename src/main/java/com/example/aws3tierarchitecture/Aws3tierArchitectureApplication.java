package com.example.aws3tierarchitecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
public class Aws3tierArchitectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(Aws3tierArchitectureApplication.class, args);
	}

}
