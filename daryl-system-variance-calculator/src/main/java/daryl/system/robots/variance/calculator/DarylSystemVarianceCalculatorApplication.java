package daryl.system.robots.variance.calculator;

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

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.robots.variance.calculator.forecaster.VarianceStockPrediction;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan("daryl.system.model")
public class DarylSystemVarianceCalculatorApplication {


	@Autowired
	Logger logger;
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemVarianceCalculatorApplication.class);
		
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startForecaster(context);
	}
	
	private static void startForecaster(ConfigurableApplicationContext context) {
		
		
		ExecutorService servicio = Executors.newFixedThreadPool(30);
		//4-20-120-480
		VarianceStockPrediction cspXAUUSD = context.getBean(VarianceStockPrediction.class);
		cspXAUUSD.init(Activo.XAUUSD, Timeframes.PERIOD_H4, "VARIANCE_XAUUSD_240", 120);
		servicio.submit(cspXAUUSD);
		System.out.println("cspXAUUSD cargado");
		
		VarianceStockPrediction cspGDAXI = context.getBean(VarianceStockPrediction.class);
		cspGDAXI.init(Activo.GDAXI, Timeframes.PERIOD_H4, "VARIANCE_GDAXI_240", 120);
		servicio.submit(cspGDAXI);
		System.out.println("cspGDAXI cargado");
		
		VarianceStockPrediction cspNDX = context.getBean(VarianceStockPrediction.class);
		cspNDX.init(Activo.NDX, Timeframes.PERIOD_H4, "VARIANCE_NDX_240", 120);
		servicio.submit(cspNDX);
		System.out.println("cspNDX cargado");
		
	}

}
