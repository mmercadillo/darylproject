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

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.robots.rna.calculator.forecaster.RnaForecasterGenerator;

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
		
		int maxNeuronasEntrada = 10;
		int maxCapasOcultas = 5;
		int maxIteraciones = 5000;
		double errorMaximo = 0.0000001;
		
		
		ExecutorService servicio = Executors.newFixedThreadPool(24);

		RnaForecasterGenerator rfgGDAXI60 = context.getBean(RnaForecasterGenerator.class);
		rfgGDAXI60.init("RNA_GDAXI_60", Activo.GDAXI, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgGDAXI60);
		
		RnaForecasterGenerator rfgGDAXI240 = context.getBean(RnaForecasterGenerator.class);
		rfgGDAXI240.init("RNA_GDAXI_240", Activo.GDAXI, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgGDAXI240);
		
		RnaForecasterGenerator rfgGDAXI1440 = context.getBean(RnaForecasterGenerator.class);
		rfgGDAXI1440.init("RNA_GDAXI_1440", Activo.GDAXI, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgGDAXI1440);
		
		RnaForecasterGenerator rfgGDAXI10080 = context.getBean(RnaForecasterGenerator.class);
		rfgGDAXI10080.init("RNA_GDAXI_10080", Activo.GDAXI, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgGDAXI10080);
		
		
		RnaForecasterGenerator rfgNDX60 = context.getBean(RnaForecasterGenerator.class);
		rfgNDX60.init("RNA_NDX_60", Activo.NDX, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX60);
		
		RnaForecasterGenerator rfgNDX240 = context.getBean(RnaForecasterGenerator.class);
		rfgNDX240.init("RNA_NDX_240", Activo.NDX, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX240);
		
		RnaForecasterGenerator rfgNDX1440 = context.getBean(RnaForecasterGenerator.class);
		rfgNDX1440.init("RNA_NDX_1440", Activo.NDX, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX1440);
		
		RnaForecasterGenerator rfgNDX10080 = context.getBean(RnaForecasterGenerator.class);
		rfgNDX10080.init("RNA_NDX_10080", Activo.NDX, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX10080);
		
		RnaForecasterGenerator rfgXAUSUD60 = context.getBean(RnaForecasterGenerator.class);
		rfgXAUSUD60.init("RNA_XAUUSD_60", Activo.XAUUSD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD60);
		
		RnaForecasterGenerator rfgXAUSUD240 = context.getBean(RnaForecasterGenerator.class);
		rfgXAUSUD240.init("RNA_XAUUSD_240", Activo.XAUUSD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD240);
		
		RnaForecasterGenerator rfgXAUSUD1440 = context.getBean(RnaForecasterGenerator.class);
		rfgXAUSUD1440.init("RNA_XAUUSD_1440", Activo.XAUUSD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD1440);
		
		RnaForecasterGenerator rfgXAUSUD10080 = context.getBean(RnaForecasterGenerator.class);
		rfgXAUSUD10080.init("RNA_XAUUSD_10080", Activo.XAUUSD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD10080);
		
		RnaForecasterGenerator rfgEURUSD60 = context.getBean(RnaForecasterGenerator.class);
		rfgEURUSD60.init("RNA_EURUSD_60", Activo.EURUSD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD60);
		
		RnaForecasterGenerator rfgEURUSD240 = context.getBean(RnaForecasterGenerator.class);
		rfgEURUSD240.init("RNA_EURUSD_240", Activo.EURUSD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD240);
		
		RnaForecasterGenerator rfgEURUSD1440 = context.getBean(RnaForecasterGenerator.class);
		rfgEURUSD1440.init("RNA_EURUSD_1440", Activo.EURUSD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD1440);
		
		RnaForecasterGenerator rfgEURUSD10080 = context.getBean(RnaForecasterGenerator.class);
		rfgEURUSD10080.init("RNA_EURUSD_10080", Activo.EURUSD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD10080);
		
		RnaForecasterGenerator rfgAUDCAD60 = context.getBean(RnaForecasterGenerator.class);
		rfgAUDCAD60.init("RNA_AUDCAD_60", Activo.AUDCAD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD60);
		
		RnaForecasterGenerator rfgAUDCAD240 = context.getBean(RnaForecasterGenerator.class);
		rfgAUDCAD240.init("RNA_AUDCAD_240", Activo.AUDCAD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD240);
		
		RnaForecasterGenerator rfgAUDCAD1440 = context.getBean(RnaForecasterGenerator.class);
		rfgAUDCAD1440.init("RNA_AUDCAD_1440", Activo.AUDCAD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD1440);
		
		RnaForecasterGenerator rfgAUDCAD10080 = context.getBean(RnaForecasterGenerator.class);
		rfgAUDCAD10080.init("RNA_AUDCAD_10080", Activo.AUDCAD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD10080);
		
		RnaForecasterGenerator rfgWTI60 = context.getBean(RnaForecasterGenerator.class);
		rfgWTI60.init("RNA_WTI_60", Activo.XTIUSD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI60);
		
		RnaForecasterGenerator rfgWTI240 = context.getBean(RnaForecasterGenerator.class);
		rfgWTI240.init("RNA_WTI_240", Activo.XTIUSD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI240);
		
		RnaForecasterGenerator rfgWTI1440 = context.getBean(RnaForecasterGenerator.class);
		rfgWTI1440.init("RNA_WTI_1440", Activo.XTIUSD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI1440);
		
		RnaForecasterGenerator rfgWTI10080 = context.getBean(RnaForecasterGenerator.class);
		rfgWTI10080.init("RNA_WTI_10080", Activo.XTIUSD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxCapasOcultas, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI10080);
		
		servicio.shutdown();
		
	}

}
