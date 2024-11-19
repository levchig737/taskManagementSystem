package io.chekarev.taskManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "io.chekarev.taskManagementSystem.domain.entities")
@EnableJpaRepositories(basePackages = "io.chekarev.taskManagementSystem.repositories")
public class TaskManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementSystemApplication.class, args);
	}

}
