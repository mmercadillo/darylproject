package daryl.system.robots.client.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@SpringBootApplication
public class DarylSystemClientWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarylSystemClientWebApplication.class, args);
	}
	
	
	@Bean("restClient")
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}

}
