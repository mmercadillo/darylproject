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
	
	
	private static final Boolean ACTIVE_60 = Boolean.FALSE;
	private static final Boolean ACTIVE_240 = Boolean.FALSE;
	private static final Boolean ACTIVE_1440 = Boolean.TRUE;
	private static final Boolean ACTIVE_10080 = Boolean.TRUE;
	
	
	private static void startForecaster(ConfigurableApplicationContext context) {
		
		int pagina = 500;
		
		ExecutorService servicio = Executors.newFixedThreadPool(25);
		
		if(DarylSystemVarianceCalculatorApplication.ACTIVE_60 == Boolean.TRUE) {
			
			VarianceStockPrediction cspXAUUSD60 = context.getBean(VarianceStockPrediction.class);
			cspXAUUSD60.init(Activo.XAUUSD, Timeframes.PERIOD_H1, "VARIANCE_XAUUSD_60", 480, pagina);
			servicio.submit(cspXAUUSD60);
			System.out.println("cspXAUUSD60 cargado");
			
			VarianceStockPrediction cspGDAXI60 = context.getBean(VarianceStockPrediction.class);
			cspGDAXI60.init(Activo.GDAXI, Timeframes.PERIOD_H4, "VARIANCE_GDAXI_60", 480, pagina);
			servicio.submit(cspGDAXI60);
			System.out.println("cspGDAXI60 cargado");
			
			VarianceStockPrediction cspNDX60 = context.getBean(VarianceStockPrediction.class);
			cspNDX60.init(Activo.NDX, Timeframes.PERIOD_H1, "VARIANCE_NDX_60", 480, pagina);
			servicio.submit(cspNDX60);
			System.out.println("cspNDX60 cargado");
			
			VarianceStockPrediction cspXTIUSD60 = context.getBean(VarianceStockPrediction.class);
			cspXTIUSD60.init(Activo.XTIUSD, Timeframes.PERIOD_H1, "VARIANCE_WTI_60", 480, pagina);
			servicio.submit(cspXTIUSD60);
			System.out.println("cspXTIUSD60 cargado");
			
			VarianceStockPrediction cspEURUSD60 = context.getBean(VarianceStockPrediction.class);
			cspEURUSD60.init(Activo.EURUSD, Timeframes.PERIOD_H1, "VARIANCE_EURUSD_60", 480, pagina);
			servicio.submit(cspEURUSD60);
			System.out.println("cspEURUSD60 cargado");
			
			VarianceStockPrediction cspAUDCAD60 = context.getBean(VarianceStockPrediction.class);
			cspAUDCAD60.init(Activo.AUDCAD, Timeframes.PERIOD_H1, "VARIANCE_AUDCAD_60", 480, pagina);
			servicio.submit(cspAUDCAD60);
			System.out.println("cspAUDCAD60 cargado");
			
		}

		if(DarylSystemVarianceCalculatorApplication.ACTIVE_240 == Boolean.TRUE) {
			
			VarianceStockPrediction cspXAUUSD240 = context.getBean(VarianceStockPrediction.class);
			cspXAUUSD240.init(Activo.XAUUSD, Timeframes.PERIOD_H4, "VARIANCE_XAUUSD_240", 120, pagina);
			servicio.submit(cspXAUUSD240);
			System.out.println("cspXAUUSD240 cargado");
			
			VarianceStockPrediction cspGDAXI240 = context.getBean(VarianceStockPrediction.class);
			cspGDAXI240.init(Activo.GDAXI, Timeframes.PERIOD_H4, "VARIANCE_GDAXI_240", 120, pagina);
			servicio.submit(cspGDAXI240);
			System.out.println("cspGDAXI240 cargado");
			
			VarianceStockPrediction cspNDX240 = context.getBean(VarianceStockPrediction.class);
			cspNDX240.init(Activo.NDX, Timeframes.PERIOD_H4, "VARIANCE_NDX_240", 120, pagina);
			servicio.submit(cspNDX240);
			System.out.println("cspNDX240 cargado");
			
			VarianceStockPrediction cspXTIUSD240 = context.getBean(VarianceStockPrediction.class);
			cspXTIUSD240.init(Activo.XTIUSD, Timeframes.PERIOD_H4, "VARIANCE_WTI_240", 120, pagina);
			servicio.submit(cspXTIUSD240);
			System.out.println("cspXTIUSD240 cargado");
			

			VarianceStockPrediction cspEURUSD240 = context.getBean(VarianceStockPrediction.class);
			cspEURUSD240.init(Activo.EURUSD, Timeframes.PERIOD_H4, "VARIANCE_EURUSD_240", 120, pagina);
			servicio.submit(cspEURUSD240);
			System.out.println("cspEURUSD240 cargado");
			
			VarianceStockPrediction cspAUDCAD240 = context.getBean(VarianceStockPrediction.class);
			cspAUDCAD240.init(Activo.AUDCAD, Timeframes.PERIOD_H4, "VARIANCE_AUDCAD_240", 120, pagina);
			servicio.submit(cspAUDCAD240);
			System.out.println("cspAUDCAD240 cargado");
			
		}

		if(DarylSystemVarianceCalculatorApplication.ACTIVE_1440 == Boolean.TRUE) {
			
			VarianceStockPrediction cspXAUUSD1440 = context.getBean(VarianceStockPrediction.class);
			cspXAUUSD1440.init(Activo.XAUUSD, Timeframes.PERIOD_D1, "VARIANCE_XAUUSD_1440", 20, pagina);
			servicio.submit(cspXAUUSD1440);
			System.out.println("cspXAUUSD1440 cargado");
			
			VarianceStockPrediction cspGDAXI1440 = context.getBean(VarianceStockPrediction.class);
			cspGDAXI1440.init(Activo.GDAXI, Timeframes.PERIOD_D1, "VARIANCE_GDAXI_1440", 20, pagina);
			servicio.submit(cspGDAXI1440);
			System.out.println("cspGDAXI1440 cargado");
			
			VarianceStockPrediction cspNDX1440 = context.getBean(VarianceStockPrediction.class);
			cspNDX1440.init(Activo.NDX, Timeframes.PERIOD_D1, "VARIANCE_NDX_1440", 20, pagina);
			servicio.submit(cspNDX1440);
			System.out.println("cspNDX1440 cargado");
			
			VarianceStockPrediction cspXTIUSD1440 = context.getBean(VarianceStockPrediction.class);
			cspXTIUSD1440.init(Activo.XTIUSD, Timeframes.PERIOD_D1, "VARIANCE_WTI_1440", 20, pagina);
			servicio.submit(cspXTIUSD1440);
			System.out.println("cspXTIUSD1440 cargado");
			
			
			VarianceStockPrediction cspEURUSD1440 = context.getBean(VarianceStockPrediction.class);
			cspEURUSD1440.init(Activo.EURUSD, Timeframes.PERIOD_D1, "VARIANCE_EURUSD_1440", 20, pagina);
			servicio.submit(cspEURUSD1440);
			System.out.println("cspEURUSD1440 cargado");
			
			VarianceStockPrediction cspAUDCAD1440 = context.getBean(VarianceStockPrediction.class);
			cspAUDCAD1440.init(Activo.AUDCAD, Timeframes.PERIOD_D1, "VARIANCE_AUDCAD_1440", 20, pagina);
			servicio.submit(cspAUDCAD1440);
			System.out.println("cspAUDCAD1440 cargado");
			
		}

		
		if(DarylSystemVarianceCalculatorApplication.ACTIVE_10080 == Boolean.TRUE) {
			
			VarianceStockPrediction cspXAUUSD10080 = context.getBean(VarianceStockPrediction.class);
			cspXAUUSD10080.init(Activo.XAUUSD, Timeframes.PERIOD_W1, "VARIANCE_XAUUSD_10080", 4, pagina);
			servicio.submit(cspXAUUSD10080);
			System.out.println("cspXAUUSD10080 cargado");
			
			VarianceStockPrediction cspGDAXI10080 = context.getBean(VarianceStockPrediction.class);
			cspGDAXI10080.init(Activo.GDAXI, Timeframes.PERIOD_W1, "VARIANCE_GDAXI_10080", 4, pagina);
			servicio.submit(cspGDAXI10080);
			System.out.println("cspGDAXI10080 cargado");
			
			VarianceStockPrediction cspNDX10080 = context.getBean(VarianceStockPrediction.class);
			cspNDX10080.init(Activo.NDX, Timeframes.PERIOD_W1, "VARIANCE_NDX_10080", 4, pagina);
			servicio.submit(cspNDX10080);
			System.out.println("cspNDX10080 cargado");
			
			VarianceStockPrediction cspXTIUSD10080 = context.getBean(VarianceStockPrediction.class);
			cspXTIUSD10080.init(Activo.XTIUSD, Timeframes.PERIOD_W1, "VARIANCE_WTI_10080", 4, pagina);
			servicio.submit(cspXTIUSD10080);
			System.out.println("cspXTIUSD10080 cargado");
			
			VarianceStockPrediction cspEURUSD10080 = context.getBean(VarianceStockPrediction.class);
			cspEURUSD10080.init(Activo.EURUSD, Timeframes.PERIOD_W1, "VARIANCE_EURUSD_10080", 4, pagina);
			servicio.submit(cspEURUSD10080);
			System.out.println("cspEURUSD10080 cargado");
			
			VarianceStockPrediction cspAUDCAD10080 = context.getBean(VarianceStockPrediction.class);
			cspAUDCAD10080.init(Activo.AUDCAD, Timeframes.PERIOD_W1, "VARIANCE_AUDCAD_10080", 4, pagina);
			servicio.submit(cspAUDCAD10080);
			System.out.println("cspAUDCAD10080 cargado");
			
			
		}

		
		servicio.shutdown();
		
	}

}
