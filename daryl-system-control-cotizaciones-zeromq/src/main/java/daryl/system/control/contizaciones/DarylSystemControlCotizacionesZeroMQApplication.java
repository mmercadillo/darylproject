package daryl.system.control.contizaciones;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import daryl.system.control.contizaciones.zeromq.control.ControlCotizaciones;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EntityScan("daryl.system.model")
@EnableJms
@EnableTransactionManagement
public class DarylSystemControlCotizacionesZeroMQApplication {

	
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
