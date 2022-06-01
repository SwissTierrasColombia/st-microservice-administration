package com.ai.st.microservice.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients(basePackages = { "com.ai.st.microservice.common.clients" })
@EnableEurekaClient
@ComponentScan(basePackages = { "com.ai.st.microservice.common.business", "com.ai.st.microservice.administration" })
public class StMicroserviceAdministrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StMicroserviceAdministrationApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
