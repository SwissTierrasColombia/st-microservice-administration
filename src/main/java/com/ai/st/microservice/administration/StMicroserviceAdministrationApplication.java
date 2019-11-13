package com.ai.st.microservice.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StMicroserviceAdministrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StMicroserviceAdministrationApplication.class, args);
	}

}
