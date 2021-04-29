package daryl.system.robots.rna.calculator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan("daryl.system.model")
public class DarylSystemRnaCalculatorApplication {


	@Autowired
	Logger logger;
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemRnaCalculatorApplication.class);
		
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startForecaster(context);
	}
	
	private static void startForecaster(ConfigurableApplicationContext context) {
		
		
		ExecutorService servicio = Executors.newFixedThreadPool(30);
		//4-20-120-480
		
		
		servicio.shutdown();
		
	}

}
