package daryl.system.robots.arima.c.calculator.close;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.CombinacionArimaC;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.arima.c.calculator.close.forecaster.ArimaForecasterGenerator;
import daryl.system.robots.arima.c.calculator.close.repository.ICombinacionesArimaCRepository;
import daryl.system.robots.arima.c.calculator.close.repository.IHistoricoRepository;

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

	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	
	private static List<Datos> toDatosList(List<Historico> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (Historico hist : historico) {
			
			Datos dato = Datos.builder().fecha(hist.getFecha())
										.hora(hist.getHora())
										.apertura(hist.getApertura())
										.maximo(hist.getMaximo())
										.minimo(hist.getMinimo())
										.cierre(hist.getCierre())
										.volumen(hist.getVolumen())
										.build();
			datos.add(dato);
			
		}
		
		return datos;
		
		
	}
	
	
	static List<Double> histXAUUSD60 = null;
	static List<Double> histXAUUSD240 = null;
	static List<Double> histXAUUSD1440 = null;
	static List<Double> histXAUUSD10080 = null;

	static List<Double> histNDX60 = null;
	static List<Double> histNDX240 = null;
	static List<Double> histNDX1440 = null;
	static List<Double> histNDX10080 = null;

	static List<Double> histGDAXI60 = null;
	static List<Double> histGDAXI240 = null;
	static List<Double> histGDAXI1440 = null;
	static List<Double> histGDAXI10080 = null;

	static List<Double> histEURUSD60 = null;
	static List<Double> histEURUSD240 = null;
	static List<Double> histEURUSD1440 = null;
	static List<Double> histEURUSD10080 = null;

	static List<Double> histAUDCAD60 = null;
	static List<Double> histAUDCAD240 = null;
	static List<Double> histAUDCAD1440 = null;
	static List<Double> histAUDCAD10080 = null;

	static List<Double> histWTI60 = null;
	static List<Double> histWTI240 = null;
	static List<Double> histWTI1440 = null;
	
    private static void startForecaster(ConfigurableApplicationContext context) {
    	
		ICombinacionesArimaCRepository combinacionesArimaCRepository = context.getBean(ICombinacionesArimaCRepository.class);
		IHistoricoRepository historicoRepository = context.getBean(IHistoricoRepository.class);
		 
    	

		List<CombinacionArimaC> combinacionesFile = combinacionesArimaCRepository.findAll();
		Collections.reverse(combinacionesFile);
		

		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.XAUUSD);
		List<Datos> datosForecast = toDatosList(historico);
		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histXAUUSD60 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de XAUUSD 60");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H4, Activo.XAUUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histXAUUSD240 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de XAUUSD 240");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_D1, Activo.XAUUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histXAUUSD1440 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de XAUUSD 1440");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_W1, Activo.XAUUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histXAUUSD10080 = darylNormalizer.getDatos();		
		System.out.println("Cargado historico de XAUUSD 10080");
		
    	ExecutorService servicio = Executors.newFixedThreadPool(25);

    	int maxDesviaciones = 5;
    	int inicio = 1000;

		ArimaForecasterGenerator afgXAUUSD_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_60.init("ARIMA_C_XAUUSD_60", "ARIMA_C_XAUUSD_60", Activo.XAUUSD, Timeframes.PERIOD_H1, maxDesviaciones, inicio, histXAUUSD60, combinacionesFile); // <-- here
		servicio.submit(afgXAUUSD_60); 		
		
	    ArimaForecasterGenerator afgXAUUSD_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_240.init("ARIMA_C_XAUUSD_240", "ARIMA_C_XAUUSD_240", Activo.XAUUSD, Timeframes.PERIOD_H4, maxDesviaciones, inicio, histXAUUSD240, combinacionesFile); // <-- here
		servicio.submit(afgXAUUSD_240);   
		
	    ArimaForecasterGenerator afgXAUUSD_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_1440.init("ARIMA_C_XAUUSD_1440", "ARIMA_C_XAUUSD_1440", Activo.XAUUSD, Timeframes.PERIOD_D1, maxDesviaciones, inicio, histXAUUSD1440, combinacionesFile); // <-- here
		servicio.submit(afgXAUUSD_1440);	
		
    	ArimaForecasterGenerator afgXAUUSD_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgXAUUSD_10080.init("ARIMA_C_XAUUSD_10080", "ARIMA_C_XAUUSD_10080", Activo.XAUUSD, Timeframes.PERIOD_W1, maxDesviaciones, inicio, histXAUUSD10080, combinacionesFile); // <-- here
		servicio.submit(afgXAUUSD_10080);	
	

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.NDX);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histNDX60 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de NDX 60");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H4, Activo.NDX);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histNDX240 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de NDX 240");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_D1, Activo.NDX);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histNDX1440 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de NDX 1440");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_W1, Activo.NDX);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histNDX10080 = darylNormalizer.getDatos();		
		System.out.println("Cargado historico de NDX 10080");
    	
    		
	    ArimaForecasterGenerator afgNDX_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_60.init("ARIMA_C_NDX_60", "ARIMA_C_NDX_60", Activo.NDX, Timeframes.PERIOD_H1, maxDesviaciones, inicio, histNDX60, combinacionesFile); // <-- here
		servicio.submit(afgNDX_60);
		
	    ArimaForecasterGenerator afgNDX_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_240.init("ARIMA_C_NDX_240", "ARIMA_C_NDX_240", Activo.NDX, Timeframes.PERIOD_H4, maxDesviaciones, inicio, histNDX240, combinacionesFile); // <-- here
		servicio.submit(afgNDX_240);
		
	    ArimaForecasterGenerator afgNDX_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_1440.init("ARIMA_C_NDX_1440", "ARIMA_C_NDX_1440", Activo.NDX, Timeframes.PERIOD_D1, maxDesviaciones, inicio, histNDX1440, combinacionesFile); // <-- here
		servicio.submit(afgNDX_1440);	
		
	    ArimaForecasterGenerator afgNDX_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgNDX_10080.init("ARIMA_C_NDX_10080", "ARIMA_C_NDX_10080", Activo.NDX, Timeframes.PERIOD_W1, maxDesviaciones, inicio, histNDX10080, combinacionesFile); // <-- here
		servicio.submit(afgNDX_10080);	
    		

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	    	
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.GDAXI);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histGDAXI60 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de GDAXI 60");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H4, Activo.GDAXI);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histGDAXI240 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de GDAXI 240");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_D1, Activo.GDAXI);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histGDAXI1440 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de GDAXI 1440");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_W1, Activo.GDAXI);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histGDAXI10080 = darylNormalizer.getDatos();		
		System.out.println("Cargado historico de GDAXI 10080");
    	
    		
	    ArimaForecasterGenerator afgGDAXI_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_60.init("ARIMA_C_GDAXI_60", "ARIMA_C_GDAXI_60", Activo.GDAXI, Timeframes.PERIOD_H1, maxDesviaciones, inicio, histGDAXI60, combinacionesFile); // <-- here
		servicio.submit(afgGDAXI_60);
		
	    ArimaForecasterGenerator afgGDAXI_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_240.init("ARIMA_C_GDAXI_240", "ARIMA_C_GDAXI_240", Activo.GDAXI, Timeframes.PERIOD_H4, maxDesviaciones, inicio, histGDAXI240, combinacionesFile); // <-- here
		servicio.submit(afgGDAXI_240);
		
	    ArimaForecasterGenerator afgGDAXI_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_1440.init("ARIMA_C_GDAXI_1440", "ARIMA_C_GDAXI_1440", Activo.GDAXI, Timeframes.PERIOD_D1, maxDesviaciones, inicio, histGDAXI1440, combinacionesFile); // <-- here
		servicio.submit(afgGDAXI_1440);	
		
	    ArimaForecasterGenerator afgGDAXI_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgGDAXI_10080.init("ARIMA_C_GDAXI_10080", "ARIMA_C_GDAXI_10080", Activo.GDAXI, Timeframes.PERIOD_W1, maxDesviaciones, inicio, histGDAXI10080, combinacionesFile); // <-- here
	    servicio.submit(afgGDAXI_10080);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.XTIUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histWTI60 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de XTIUSD 60");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H4, Activo.XTIUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histWTI240 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de XTIUSD 240");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_D1, Activo.XTIUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histWTI1440 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de XTIUSD 1440");
		
    		
	    ArimaForecasterGenerator afgWTI_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgWTI_60.init("ARIMA_C_WTI_60", "ARIMA_C_WTI_60", Activo.XTIUSD, Timeframes.PERIOD_H1, maxDesviaciones, inicio, histWTI60, combinacionesFile); // <-- here
		servicio.submit(afgWTI_60);
		
	    ArimaForecasterGenerator afgWTI_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgWTI_240.init("ARIMA_C_WTI_240", "ARIMA_C_WTI_240", Activo.XTIUSD, Timeframes.PERIOD_H4, maxDesviaciones, inicio, histWTI240, combinacionesFile); // <-- here
		servicio.submit(afgWTI_240);
		
	    ArimaForecasterGenerator afgWTI_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgWTI_1440.init("ARIMA_C_WTI_1440", "ARIMA_C_WTI_1440", Activo.XTIUSD, Timeframes.PERIOD_D1, maxDesviaciones, inicio, histWTI1440, combinacionesFile); // <-- here
		servicio.submit(afgWTI_1440);	
    		

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.EURUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histEURUSD60 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de EURUSD 60");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H4, Activo.EURUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histEURUSD240 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de EURUSD 240");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_D1, Activo.EURUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histEURUSD1440 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de EURUSD 1440");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_W1, Activo.EURUSD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histEURUSD10080 = darylNormalizer.getDatos();		
		System.out.println("Cargado historico de EURUSD 10080");
    	
    		
	    ArimaForecasterGenerator afgEURUSD_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_60.init("ARIMA_C_EURUSD_60", "ARIMA_C_EURUSD_60", Activo.EURUSD, Timeframes.PERIOD_H1, maxDesviaciones, inicio, histEURUSD60, combinacionesFile); // <-- here
		servicio.submit(afgEURUSD_60);
		
	    ArimaForecasterGenerator afgEURUSD_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_240.init("ARIMA_C_EURUSD_240", "ARIMA_C_EURUSD_240", Activo.EURUSD, Timeframes.PERIOD_H4, maxDesviaciones, inicio, histEURUSD240, combinacionesFile); // <-- here
		servicio.submit(afgEURUSD_240);
		
	    ArimaForecasterGenerator afgEURUSD_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_1440.init("ARIMA_C_EURUSD_1440", "ARIMA_C_EURUSD_1440", Activo.EURUSD, Timeframes.PERIOD_D1, maxDesviaciones, inicio, histEURUSD1440, combinacionesFile); // <-- here
		servicio.submit(afgEURUSD_1440);
		
	    ArimaForecasterGenerator afgEURUSD_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgEURUSD_10080.init("ARIMA_C_EURUSD_10080", "ARIMA_C_EURUSD_10080", Activo.EURUSD, Timeframes.PERIOD_W1, maxDesviaciones, inicio, histEURUSD10080, combinacionesFile); // <-- here
		servicio.submit(afgEURUSD_10080);	
    		
    	
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.AUDCAD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histAUDCAD60 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de AUDCAD 60");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H4, Activo.AUDCAD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histAUDCAD240 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de AUDCAD 240");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_D1, Activo.AUDCAD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histAUDCAD1440 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de AUDCAD 1440");
		
		historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_W1, Activo.AUDCAD);
		datosForecast = toDatosList(historico);
		darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		histAUDCAD10080 = darylNormalizer.getDatos();		
		System.out.println("Cargado historico de AUDCAD 10080");
    	
    		
	    ArimaForecasterGenerator afgAUDCAD_60 = context.getBean(ArimaForecasterGenerator.class);
	    afgAUDCAD_60.init("ARIMA_C_AUDCAD_60", "ARIMA_C_AUDCAD_60", Activo.AUDCAD, Timeframes.PERIOD_H1, maxDesviaciones, inicio, histAUDCAD60, combinacionesFile); // <-- here
		servicio.submit(afgAUDCAD_60);
		
	    ArimaForecasterGenerator afgAUDCAD_240 = context.getBean(ArimaForecasterGenerator.class);
	    afgAUDCAD_240.init("ARIMA_C_AUDCAD_240", "ARIMA_C_AUDCAD_240", Activo.AUDCAD, Timeframes.PERIOD_H4, maxDesviaciones, inicio, histAUDCAD240, combinacionesFile); // <-- here
		servicio.submit(afgAUDCAD_240);
		
	    ArimaForecasterGenerator afgAUDCAD_1440 = context.getBean(ArimaForecasterGenerator.class);
	    afgAUDCAD_1440.init("ARIMA_C_AUDCAD_1440", "ARIMA_C_AUDCAD_1440", Activo.AUDCAD, Timeframes.PERIOD_D1, maxDesviaciones, inicio, histAUDCAD1440, combinacionesFile); // <-- here
		servicio.submit(afgAUDCAD_1440);
		
	    ArimaForecasterGenerator afgAUDCAD_10080 = context.getBean(ArimaForecasterGenerator.class);
	    afgAUDCAD_10080.init("ARIMA_C_AUDCAD_10080","ARIMA_C_AUDCAD_10080", Activo.AUDCAD, Timeframes.PERIOD_W1, maxDesviaciones, inicio, histAUDCAD10080, combinacionesFile); // <-- here
		servicio.submit(afgAUDCAD_10080);

    	
    	servicio.shutdown();
    }
	
	
}
