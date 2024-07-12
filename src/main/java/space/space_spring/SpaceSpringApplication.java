package space.space_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class SpaceSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceSpringApplication.class, args);
	}

}
