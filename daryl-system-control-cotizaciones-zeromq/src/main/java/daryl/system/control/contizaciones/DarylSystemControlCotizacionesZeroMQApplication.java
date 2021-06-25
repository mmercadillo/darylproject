package daryl.system.control.contizaciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import daryl.system.control.contizaciones.zeromq.control.ControlCotizaciones;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EntityScan("daryl.system.model")
@EnableJms
@EnableTransactionManagement
@EnableScheduling
public class DarylSystemControlCotizacionesZeroMQApplication {

	@Autowired
	Logger logger;
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemControlCotizacionesZeroMQApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startForecaster(context);
	    
	}
	
    private static void startForecaster(ConfigurableApplicationContext context) {
   
    	ControlCotizaciones controlCoticiones = context.getBean(ControlCotizaciones.class);
    	controlCoticiones.start();

    }


}
