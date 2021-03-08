package daryl.system.robots.arima.c.calculator.close;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan("daryl.system.model")
public class DarylSystemArimaCCalculatoCloseApplication {

	@Autowired
	Logger logger;
	
	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemArimaCCalculatoCloseApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startForecaster(context);
	}

    private static void startForecaster(ConfigurableApplicationContext context) {
    	
    	ExecutorService servicio = Executors.newFixedThreadPool(30);

	    
	    /*
	    ArimaForecasterGenerator afgXAUUSD_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_10080.init(Estrategia.ARIMA_C_XAUUSD_10080, TipoRobot.ARIMA_C_XAUUSD_10080, TipoActivo.XAUUSD, Timeframes.PERIOD_W1, 4, 4); // <-- here
		servicio.submit(afgXAUUSD_10080);
		
	    ArimaForecasterGenerator afgXAUUSD_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_1440.init(Estrategia.ARIMA_C_XAUUSD_1440, TipoRobot.ARIMA_C_XAUUSD_1440, TipoActivo.XAUUSD, Timeframes.PERIOD_D1, 3, 20); // <-- here
		servicio.submit(afgXAUUSD_1440);	
		
	    ArimaForecasterGenerator afgXAUUSD_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_240.init(Estrategia.ARIMA_C_XAUUSD_240, TipoRobot.ARIMA_C_XAUUSD_240, TipoActivo.XAUUSD, Timeframes.PERIOD_H4, 2, 120); // <-- here
		servicio.submit(afgXAUUSD_240);
		
	    ArimaForecasterGenerator afgXAUUSD_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_60.init(Estrategia.ARIMA_C_XAUUSD_60, TipoRobot.ARIMA_C_XAUUSD_60, TipoActivo.XAUUSD, Timeframes.PERIOD_H1, 0, 480); // <-- here
		servicio.submit(afgXAUUSD_60);
		
		
	    ArimaForecasterGenerator afgNDX_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_10080.init(Estrategia.ARIMA_C_NDX_10080, TipoRobot.ARIMA_C_NDX_10080, TipoActivo.NDX, Timeframes.PERIOD_W1, 4, 4); // <-- here
		servicio.submit(afgNDX_10080);
		
	    ArimaForecasterGenerator afgNDX_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_1440.init(Estrategia.ARIMA_C_NDX_1440, TipoRobot.ARIMA_C_NDX_1440, TipoActivo.NDX, Timeframes.PERIOD_D1, 3, 20); // <-- here
		servicio.submit(afgNDX_1440);	
		
	    ArimaForecasterGenerator afgNDX_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_240.init(Estrategia.ARIMA_C_NDX_240, TipoRobot.ARIMA_C_NDX_240, TipoActivo.NDX, Timeframes.PERIOD_H4, 2, 120); // <-- here
		servicio.submit(afgNDX_240);
		
	    ArimaForecasterGenerator afgNDX_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_60.init(Estrategia.ARIMA_C_NDX_60, TipoRobot.ARIMA_C_NDX_60, TipoActivo.NDX, Timeframes.PERIOD_H1, 0, 480); // <-- here
		servicio.submit(afgNDX_60);
		
	    ArimaForecasterGenerator afgGDAXI_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_10080.init(Estrategia.ARIMA_C_GDAXI_10080, TipoRobot.ARIMA_C_GDAXI_10080, TipoActivo.GDAXI, Timeframes.PERIOD_W1, 4, 4); // <-- here
	    servicio.submit(afgGDAXI_10080);
		
	    ArimaForecasterGenerator afgGDAXI_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_1440.init(Estrategia.ARIMA_C_GDAXI_1440, TipoRobot.ARIMA_C_GDAXI_1440, TipoActivo.GDAXI, Timeframes.PERIOD_D1, 3, 20); // <-- here
		servicio.submit(afgGDAXI_1440);	
		
	    ArimaForecasterGenerator afgGDAXI_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_240.init(Estrategia.ARIMA_C_GDAXI_240, TipoRobot.ARIMA_C_GDAXI_240, TipoActivo.GDAXI, Timeframes.PERIOD_H4, 2, 120); // <-- here
		servicio.submit(afgGDAXI_240);
		
	    ArimaForecasterGenerator afgGDAXI_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_60.init(Estrategia.ARIMA_C_GDAXI_60, TipoRobot.ARIMA_C_GDAXI_60, TipoActivo.GDAXI, Timeframes.PERIOD_H1, 0, 480); // <-- here
		servicio.submit(afgGDAXI_60);
		
	    ArimaForecasterGenerator afgEURUSD_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_10080.init(Estrategia.ARIMA_C_EURUSD_10080, TipoRobot.ARIMA_C_EURUSD_10080, TipoActivo.EURUSD, Timeframes.PERIOD_W1, 4, 4); // <-- here
		servicio.submit(afgEURUSD_10080);
		
	    ArimaForecasterGenerator afgEURUSD_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_1440.init(Estrategia.ARIMA_C_EURUSD_1440, TipoRobot.ARIMA_C_EURUSD_1440, TipoActivo.EURUSD, Timeframes.PERIOD_D1, 3, 20); // <-- here
		servicio.submit(afgEURUSD_1440);	
		
	    ArimaForecasterGenerator afgEURUSD_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_240.init(Estrategia.ARIMA_C_EURUSD_240, TipoRobot.ARIMA_C_EURUSD_240, TipoActivo.EURUSD, Timeframes.PERIOD_H4, 2, 120); // <-- here
		servicio.submit(afgEURUSD_240);
		
	    ArimaForecasterGenerator afgEURUSD_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_60.init(Estrategia.ARIMA_C_EURUSD_60, TipoRobot.ARIMA_C_EURUSD_60, TipoActivo.EURUSD, Timeframes.PERIOD_H1, 0, 480); // <-- here
		servicio.submit(afgEURUSD_60);
		
		
	    //ArimaForecasterGenerator afgAUDCAD_10080 = context.getBean(ArimaForecasterGenerator.class);
	    //afgAUDCAD_10080.init(Estrategia.ARIMA_C_AUDCAD_10080, Robot.ARIMA_C_AUDCAD_10080, TipoActivo.AUDCAD, Timeframes.PERIOD_W1, 4, 4); // <-- here
		//servicio.submit(afgAUDCAD_10080);
		
	    ArimaForecasterGenerator afgAUDCAD_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgAUDCAD_1440.init(Estrategia.ARIMA_C_AUDCAD_1440, TipoRobot.ARIMA_C_AUDCAD_1440, TipoActivo.AUDCAD, Timeframes.PERIOD_D1, 3, 20); // <-- here
		//servicio.submit(afgAUDCAD_1440);	
		
	    ArimaForecasterGenerator afgAUDCAD_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgAUDCAD_240.init(Estrategia.ARIMA_C_AUDCAD_240, TipoRobot.ARIMA_C_AUDCAD_240, TipoActivo.AUDCAD, Timeframes.PERIOD_H4, 2, 120); // <-- here
		//servicio.submit(afgAUDCAD_240);
		
	    ArimaForecasterGenerator afgAUDCAD_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgAUDCAD_60.init(Estrategia.ARIMA_C_AUDCAD_60, TipoRobot.ARIMA_C_AUDCAD_60, TipoActivo.AUDCAD, Timeframes.PERIOD_H1, 0, 480); // <-- here
		//servicio.submit(afgAUDCAD_60);
		
		
	    ArimaForecasterGenerator afgWTI_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgWTI_1440.init(Estrategia.ARIMA_C_WTI_1440, TipoRobot.ARIMA_C_WTI_1440, TipoActivo.XTIUSD, Timeframes.PERIOD_D1, 3, 20); // <-- here
		servicio.submit(afgWTI_1440);	
		
	    ArimaForecasterGenerator afgWTI_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgWTI_240.init(Estrategia.ARIMA_C_WTI_240, TipoRobot.ARIMA_C_WTI_240, TipoActivo.XTIUSD, Timeframes.PERIOD_H4, 2, 120); // <-- here
		servicio.submit(afgWTI_240);
		
	    ArimaForecasterGenerator afgWTI_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgWTI_60.init(Estrategia.ARIMA_C_WTI_60, TipoRobot.ARIMA_C_WTI_60, TipoActivo.XTIUSD, Timeframes.PERIOD_H1, 0, 480); // <-- here
		servicio.submit(afgWTI_60);
    	
		servicio.shutdown();
    	*/
    }
	
	
}
