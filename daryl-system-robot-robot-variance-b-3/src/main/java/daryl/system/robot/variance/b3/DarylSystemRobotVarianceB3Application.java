package daryl.system.robot.variance.b3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EntityScan("daryl.system.model")
@EnableJms
@EnableTransactionManagement
public class DarylSystemRobotVarianceB3Application {

	public static void main(String[] args) {
		SpringApplication.run(DarylSystemRobotVarianceB3Application.class, args);
	}
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }

}
