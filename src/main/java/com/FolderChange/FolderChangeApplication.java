package com.FolderChange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FolderChangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FolderChangeApplication.class, args);
	}
}
