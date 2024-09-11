package com.personal.grievanceManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GrievanceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrievanceManagementApplication.class, args);
	}

}
