package com.bolotov.oraclebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OracleApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context =  SpringApplication.run(OracleApplication.class, args);
	}
}
