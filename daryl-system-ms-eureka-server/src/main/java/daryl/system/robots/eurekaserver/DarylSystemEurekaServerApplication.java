package daryl.system.robots.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DarylSystemEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarylSystemEurekaServerApplication.class, args);
	}

}
