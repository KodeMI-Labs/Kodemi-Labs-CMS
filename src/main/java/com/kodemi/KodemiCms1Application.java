package com.kodemi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = { "com.example.admin", "com.kodemi" })
public class KodemiCms1Application {

	public static void main(String[] args) {
		SpringApplication.run(KodemiCms1Application.class, args);
	}

}
