package com.ai.st.microservice.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ai.st.entities"})
public class StMicroserviceAdministration2Application {

	public static void main(String[] args) {
		SpringApplication.run(StMicroserviceAdministration2Application.class, args);
	}

}
