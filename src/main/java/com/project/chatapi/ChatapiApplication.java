package com.project.chatapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.project.chatapi.config.EnvPropertySourceFactory;

@SpringBootApplication
@PropertySource(value = "file:./app.env", factory = EnvPropertySourceFactory.class)
public class ChatapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatapiApplication.class, args);
	}

}
