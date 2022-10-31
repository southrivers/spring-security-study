package com.websystique.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


//@EnableAutoConfiguration
//@ComponentScan("com.websystique.springboot")
@SpringBootApplication(scanBasePackages={"com.websystique.springboot"}, exclude = { SecurityAutoConfiguration.class })// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpringBootStandAloneApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStandAloneApp.class, args);
	}
}
