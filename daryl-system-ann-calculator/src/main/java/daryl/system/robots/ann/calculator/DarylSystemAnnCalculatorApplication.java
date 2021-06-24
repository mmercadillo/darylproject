package daryl.system.robots.ann.calculator;

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
import daryl.system.robots.ann.calculator.forecaster.ANNForecasterGenerator;
import daryl.system.robots.ann.calculator.forecaster.AnnForecasterTester;


@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan("daryl.system.model")
public class DarylSystemAnnCalculatorApplication {


	@Autowired
	Logger logger;
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemAnnCalculatorApplication.class);
		
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startForecaster(context);
	    //startTester(context);
	}
	
	private static void startTester(ConfigurableApplicationContext context) {
		
		int pagina = 500;
		
		AnnForecasterTester tester = context.getBean(AnnForecasterTester.class);
		tester.calcularPrediccion("ANN_GDAXI_60", Timeframes.PERIOD_H1, Activo.GDAXI, pagina);

	}
	
	private static void startForecaster(ConfigurableApplicationContext context) {
		
		int maxNeuronasEntrada = 25;
		int maxIteraciones = 5000;
		double errorMaximo = 0.0000001;
		int pagina = 500;
		
		ExecutorService servicio = Executors.newFixedThreadPool(24);

		ANNForecasterGenerator rfgGDAXI60 = context.getBean(ANNForecasterGenerator.class);
		rfgGDAXI60.init("ANN_GDAXI_60", Activo.GDAXI, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxIteraciones, errorMaximo, pagina);
		servicio.submit(rfgGDAXI60);
		/*
		ANNForecasterGenerator rfgGDAXI240 = context.getBean(ANNForecasterGenerator.class);
		rfgGDAXI240.init("ANN_GDAXI_240", Activo.GDAXI, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgGDAXI240);
		
		ANNForecasterGenerator rfgGDAXI1440 = context.getBean(ANNForecasterGenerator.class);
		rfgGDAXI1440.init("ANN_GDAXI_1440", Activo.GDAXI, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgGDAXI1440);
		
		ANNForecasterGenerator rfgGDAXI10080 = context.getBean(ANNForecasterGenerator.class);
		rfgGDAXI10080.init("ANN_GDAXI_10080", Activo.GDAXI, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgGDAXI10080);
		
		
		ANNForecasterGenerator rfgNDX60 = context.getBean(ANNForecasterGenerator.class);
		rfgNDX60.init("ANN_NDX_60", Activo.NDX, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX60);
		
		ANNForecasterGenerator rfgNDX240 = context.getBean(ANNForecasterGenerator.class);
		rfgNDX240.init("ANN_NDX_240", Activo.NDX, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX240);
		
		ANNForecasterGenerator rfgNDX1440 = context.getBean(ANNForecasterGenerator.class);
		rfgNDX1440.init("ANN_NDX_1440", Activo.NDX, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX1440);
		
		ANNForecasterGenerator rfgNDX10080 = context.getBean(ANNForecasterGenerator.class);
		rfgNDX10080.init("ANN_NDX_10080", Activo.NDX, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgNDX10080);
		
		ANNForecasterGenerator rfgXAUSUD60 = context.getBean(ANNForecasterGenerator.class);
		rfgXAUSUD60.init("ANN_XAUUSD_60", Activo.XAUUSD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD60);
		
		ANNForecasterGenerator rfgXAUSUD240 = context.getBean(ANNForecasterGenerator.class);
		rfgXAUSUD240.init("ANN_XAUUSD_240", Activo.XAUUSD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD240);
		
		ANNForecasterGenerator rfgXAUSUD1440 = context.getBean(ANNForecasterGenerator.class);
		rfgXAUSUD1440.init("ANN_XAUUSD_1440", Activo.XAUUSD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD1440);
		
		ANNForecasterGenerator rfgXAUSUD10080 = context.getBean(ANNForecasterGenerator.class);
		rfgXAUSUD10080.init("ANN_XAUUSD_10080", Activo.XAUUSD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgXAUSUD10080);
		
		ANNForecasterGenerator rfgEURUSD60 = context.getBean(ANNForecasterGenerator.class);
		rfgEURUSD60.init("ANN_EURUSD_60", Activo.EURUSD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD60);
		
		ANNForecasterGenerator rfgEURUSD240 = context.getBean(ANNForecasterGenerator.class);
		rfgEURUSD240.init("ANN_EURUSD_240", Activo.EURUSD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD240);
		
		ANNForecasterGenerator rfgEURUSD1440 = context.getBean(ANNForecasterGenerator.class);
		rfgEURUSD1440.init("ANN_EURUSD_1440", Activo.EURUSD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD1440);
		
		ANNForecasterGenerator rfgEURUSD10080 = context.getBean(ANNForecasterGenerator.class);
		rfgEURUSD10080.init("ANN_EURUSD_10080", Activo.EURUSD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgEURUSD10080);
		
		ANNForecasterGenerator rfgAUDCAD60 = context.getBean(ANNForecasterGenerator.class);
		rfgAUDCAD60.init("ANN_AUDCAD_60", Activo.AUDCAD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD60);
		
		ANNForecasterGenerator rfgAUDCAD240 = context.getBean(ANNForecasterGenerator.class);
		rfgAUDCAD240.init("ANN_AUDCAD_240", Activo.AUDCAD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD240);
		
		ANNForecasterGenerator rfgAUDCAD1440 = context.getBean(ANNForecasterGenerator.class);
		rfgAUDCAD1440.init("ANN_AUDCAD_1440", Activo.AUDCAD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD1440);
		
		ANNForecasterGenerator rfgAUDCAD10080 = context.getBean(ANNForecasterGenerator.class);
		rfgAUDCAD10080.init("ANN_AUDCAD_10080", Activo.AUDCAD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgAUDCAD10080);
		
		ANNForecasterGenerator rfgWTI60 = context.getBean(ANNForecasterGenerator.class);
		rfgWTI60.init("ANN_WTI_60", Activo.XTIUSD, Timeframes.PERIOD_H1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI60);
		
		ANNForecasterGenerator rfgWTI240 = context.getBean(ANNForecasterGenerator.class);
		rfgWTI240.init("ANN_WTI_240", Activo.XTIUSD, Timeframes.PERIOD_H4, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI240);
		
		ANNForecasterGenerator rfgWTI1440 = context.getBean(ANNForecasterGenerator.class);
		rfgWTI1440.init("ANN_WTI_1440", Activo.XTIUSD, Timeframes.PERIOD_D1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI1440);
		
		ANNForecasterGenerator rfgWTI10080 = context.getBean(ANNForecasterGenerator.class);
		rfgWTI10080.init("ANN_WTI_10080", Activo.XTIUSD, Timeframes.PERIOD_W1, maxNeuronasEntrada, maxIteraciones, errorMaximo);
		servicio.submit(rfgWTI10080);
		*/
		servicio.shutdown();
		
	}

}
