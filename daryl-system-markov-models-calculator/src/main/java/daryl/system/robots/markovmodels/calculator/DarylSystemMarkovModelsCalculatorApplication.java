package daryl.system.robots.markovmodels.calculator;

import java.util.ArrayList;
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
import daryl.system.model.historicos.Historico;
import daryl.system.robots.markovmodels.calculator.probability.HMM;
import daryl.system.robots.markovmodels.calculator.repository.IHistoricoRepository;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan("daryl.system.model")
public class DarylSystemMarkovModelsCalculatorApplication {


	@Autowired
	Logger logger;
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemMarkovModelsCalculatorApplication.class);
		
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startForecaster(context);
	}
	
	private static void startForecaster(ConfigurableApplicationContext context) {
		
		
		ExecutorService servicio = Executors.newFixedThreadPool(30);
		//4-20-120-480
		
		IHistoricoRepository historicoRepository = context.getBean(IHistoricoRepository.class);
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.XAUUSD);
		List<Datos> datosForecast = toDatosList(historico);
		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		List<Double> histXAUUSD60 = darylNormalizer.getDatos();
		System.out.println("Cargado historico de XAUUSD 60");
		
		List<Double> sublista = histXAUUSD60.subList(0, 5);
		System.out.println(histXAUUSD60.subList(0, 6));
		
		double[] values = sublista.stream().mapToDouble(value -> value).toArray();
		
		// TODO code application logic here
        HMM hmmInstance = new HMM();
        try {
			hmmInstance.testHmmBWL();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		servicio.shutdown();
		
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
	

}
