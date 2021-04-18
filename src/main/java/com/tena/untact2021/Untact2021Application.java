package com.tena.untact2021;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Untact2021Application {

	public static void main(String[] args) {
		// ContextPath must start with '/' and not end with '/'
		// System.setProperty("server.servlet.context-path", "/untact");
		SpringApplication.run(Untact2021Application.class, args);
	}

}
