package marustuff.textometer;

import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class TextometerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextometerApplication.class, args);
	}


}
