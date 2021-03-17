package daryl.system.robot.arima.c;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@PropertySource("classpath:arima-xauusd.properties")
@PropertySource("classpath:arima-xauusd-h4.properties")
@PropertySource("classpath:arima-xauusd-d1.properties")
@PropertySource("classpath:arima-xauusd-w1.properties")
@PropertySource("classpath:arima-audcad.properties")
@PropertySource("classpath:arima-audcad-h4.properties")
@PropertySource("classpath:arima-audcad-d1.properties")
@PropertySource("classpath:arima-audcad-w1.properties")
@PropertySource("classpath:arima-eurusd.properties")
@PropertySource("classpath:arima-eurusd-h4.properties")
@PropertySource("classpath:arima-eurusd-d1.properties")
@PropertySource("classpath:arima-eurusd-w1.properties")
@PropertySource("classpath:arima-ndx.properties")
@PropertySource("classpath:arima-ndx-h4.properties")
@PropertySource("classpath:arima-ndx-d1.properties")
@PropertySource("classpath:arima-ndx-w1.properties")
@PropertySource("classpath:arima-gdaxi.properties")
@PropertySource("classpath:arima-gdaxi-h4.properties")
@PropertySource("classpath:arima-gdaxi-d1.properties")
@PropertySource("classpath:arima-gdaxi-w1.properties")
@PropertySource("classpath:arima-wti.properties")
@PropertySource("classpath:arima-wti-h4.properties")
@PropertySource("classpath:arima-wti-d1.properties")
public class DarylSystemRobotArimaCApplication {


	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemRobotArimaCApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	}

	
}
