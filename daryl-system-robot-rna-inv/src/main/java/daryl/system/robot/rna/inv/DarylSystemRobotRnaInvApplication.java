package daryl.system.robot.rna.inv;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EntityScan("daryl.system.model")
@EnableScheduling
@EnableJms
@EnableTransactionManagement
@PropertySource("classpath:rna-xauusd.properties")
@PropertySource("classpath:rna-xauusd-h4.properties")
@PropertySource("classpath:rna-xauusd-d1.properties")
@PropertySource("classpath:rna-xauusd-w1.properties")
@PropertySource("classpath:rna-audcad.properties")
@PropertySource("classpath:rna-ndx.properties")
@PropertySource("classpath:rna-ndx-h4.properties")
@PropertySource("classpath:rna-ndx-d1.properties")
@PropertySource("classpath:rna-ndx-w1.properties")
@PropertySource("classpath:rna-gdaxi.properties")
@PropertySource("classpath:rna-gdaxi-h4.properties")
@PropertySource("classpath:rna-gdaxi-d1.properties")
@PropertySource("classpath:rna-gdaxi-w1.properties")
public class DarylSystemRobotRnaInvApplication {

	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemRobotRnaInvApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	}

	
}
