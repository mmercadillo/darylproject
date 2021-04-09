package daryl.system.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@SpringBootApplication
@EnableJpaRepositories
@EntityScan("daryl.system.model")
public class DarylSystemWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarylSystemWebApplication.class, args);
	}
	

}
