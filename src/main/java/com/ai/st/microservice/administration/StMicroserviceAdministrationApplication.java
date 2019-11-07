package com.ai.st.microservice.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ai.st.entities"})
public class StMicroserviceAdministrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StMicroserviceAdministrationApplication.class, args);
	}

}
