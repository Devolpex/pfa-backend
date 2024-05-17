package com.pfa.pfabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pfa.pfabackend"})
public class PfaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PfaBackendApplication.class, args);

	}

}
