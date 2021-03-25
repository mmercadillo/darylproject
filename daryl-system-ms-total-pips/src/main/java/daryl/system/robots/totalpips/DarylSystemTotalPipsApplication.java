package daryl.system.robots.totalpips;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories
@EntityScan("daryl.system.model")
public class DarylSystemTotalPipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarylSystemTotalPipsApplication.class, args);
	}

}
