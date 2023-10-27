package com.bolotov.oraclebot;

import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.config.StaticInjector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Timestamp;

@SpringBootApplication
public class OracleApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context =  SpringApplication.run(OracleApplication.class, args);
	}
}
