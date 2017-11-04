package com.fc.a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableWebMvc
public class FcaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FcaApplication.class, args);
	}
}
