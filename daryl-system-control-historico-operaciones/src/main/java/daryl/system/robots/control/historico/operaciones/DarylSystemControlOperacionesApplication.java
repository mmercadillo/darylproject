package daryl.system.robots.control.historico.operaciones;

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

import daryl.system.robots.control.historico.operaciones.control.ControlHistoricoOperacionesDemolab;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EntityScan("daryl.system.model")
//@EnableScheduling
@EnableTransactionManagement
public class DarylSystemControlOperacionesApplication {

	@Autowired
	Logger logger;
	
	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemControlOperacionesApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    ControlHistoricoOperacionesDemolab chodl = context.getBean(ControlHistoricoOperacionesDemolab.class);
	    //ControlHistoricoOperaciones chodl = context.getBean(ControlHistoricoOperaciones.class);
	    chodl.run();
	    
	}

	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
}
