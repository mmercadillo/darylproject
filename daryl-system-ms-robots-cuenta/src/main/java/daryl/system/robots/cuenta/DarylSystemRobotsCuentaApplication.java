package daryl.system.robots.cuenta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableEurekaClient
@Configuration
@SpringBootApplication
@EnableJpaRepositories
@EntityScan("daryl.system.model")
public class DarylSystemRobotsCuentaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarylSystemRobotsCuentaApplication.class, args);
	}

}
