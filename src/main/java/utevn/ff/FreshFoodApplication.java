package utevn.ff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FreshFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreshFoodApplication.class, args);
	}

}
