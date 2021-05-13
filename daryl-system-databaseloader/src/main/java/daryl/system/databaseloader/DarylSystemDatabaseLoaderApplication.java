package daryl.system.databaseloader;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import daryl.system.comun.enums.Timeframes;
import daryl.system.databaseloader.loaders.AudCadLoader;
import daryl.system.databaseloader.loaders.CombinacionesLoader;
import daryl.system.databaseloader.loaders.EurUsdLoader;
import daryl.system.databaseloader.loaders.GdaxiLoader;
import daryl.system.databaseloader.loaders.GeneralLoader;
import daryl.system.databaseloader.loaders.NdxLoader;
import daryl.system.databaseloader.loaders.WtiLoader;
import daryl.system.databaseloader.loaders.XauUsdLoader;
import daryl.system.databaseloader.util.Combinaciones;
import daryl.system.databaseloader.util.Ordenacion;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan("daryl.system.model")
public class DarylSystemDatabaseLoaderApplication {

	
	
	@Autowired
	Logger logger;
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemDatabaseLoaderApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    //loadAudCad(context);
	    //loadEurUsd(context);
	    //loadGdaxi(context);
	    //loadNdx(context);
	    //loadWti(context);
	    //loadXauUsd(context);
	    //loadGeneral(context);
	    loadCombinaciones(context);
	}
	
	static void loadCombinaciones(ApplicationContext context) {
		
		
		String[] valores = {
				"10.0", "9.5", "9.0", "8.5", "8.0", "7.5", "7.0", "6.5", "6.0", "5.5", "5.0", "4.5", "4.0", "3.5", "3.0", "2.5", "2.0", "1.5", "1.0", 
				"-10.0", "-9.5", "-9.0", "-8.5", "-8.0", "-7.5", "-7.0", "-6.5", "-6.0", "-5.5", "-5.0", "-4.5", "-4.0", "-3.5", "-3.0", "-2.5", "-2.0", "-1.5", "-1.0",
				"0.90", "0.95", "-0.90", "-0.95",
				"0.8", "0.85", "-0.8", "-0.85",
				"0.7", "0.75", "-0.7", "-0.75",
				"0.6", "0.65", "-0.6", "-0.65",
				"0.5", "0.55", "-0.5", "-0.55",
				"0.4", "0.45", "-0.4", "-0.45",
				"0.3", "0.35", "-0.3", "-0.35",
				"0.2", "0.25", "-0.2", "-0.25",
				"0.1", "0.15", "-0.1", "-0.15",
				"0.05", "-0.05", "0.025", "-0.025",
				"0.01", "0.015", "-0.01", "-0.015",
				"0.001", "0.0015", "-0.001", "-0.0015"};
		
		List<String> lista = Arrays.asList(valores);
		for(int i = 1; i < lista.size(); i++) {
			
			List<String> sublista = lista.subList(0, i);
			
			Combinaciones com = new Combinaciones(sublista.stream().toArray(String[] :: new));
			com.combinar();

			Ordenacion ord = new Ordenacion(com.getCombinaciones());
			ord.ordenar();

			CombinacionesLoader loader = context.getBean(CombinacionesLoader.class);
			try {
				loader.load(ord.getLista());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		
	}

	static void loadAudCad(ApplicationContext context) {
	    AudCadLoader loader = context.getBean(AudCadLoader.class);
	    try {
	    	loader.load(Timeframes.PERIOD_H1);
	    	loader.load(Timeframes.PERIOD_H4);
			loader.load(Timeframes.PERIOD_D1);
			loader.load(Timeframes.PERIOD_W1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	static void loadEurUsd(ApplicationContext context) {
	    EurUsdLoader loader = context.getBean(EurUsdLoader.class);
	    try {
	    	loader.load(Timeframes.PERIOD_H1);
	    	loader.load(Timeframes.PERIOD_H4);
			loader.load(Timeframes.PERIOD_D1);
			loader.load(Timeframes.PERIOD_W1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	static void loadXauUsd(ApplicationContext context) {
	    XauUsdLoader loader = context.getBean(XauUsdLoader.class);
	    try {
	    	loader.load(Timeframes.PERIOD_H1);
	    	loader.load(Timeframes.PERIOD_H4);
			loader.load(Timeframes.PERIOD_D1);
			loader.load(Timeframes.PERIOD_W1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	static void loadGdaxi(ApplicationContext context) {
	    GdaxiLoader loader = context.getBean(GdaxiLoader.class);
	    try {
	    	loader.load(Timeframes.PERIOD_H1);
	    	loader.load(Timeframes.PERIOD_H4);
			loader.load(Timeframes.PERIOD_D1);
			loader.load(Timeframes.PERIOD_W1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	static void loadNdx(ApplicationContext context) {
	    NdxLoader loader = context.getBean(NdxLoader.class);
	    try {
	    	loader.load(Timeframes.PERIOD_H1);
	    	loader.load(Timeframes.PERIOD_H4);
			loader.load(Timeframes.PERIOD_D1);
			loader.load(Timeframes.PERIOD_W1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	static void loadWti(ApplicationContext context) {
	    WtiLoader loader = context.getBean(WtiLoader.class);
	    try {
	    	loader.load(Timeframes.PERIOD_H1);
	    	loader.load(Timeframes.PERIOD_H4);
			loader.load(Timeframes.PERIOD_D1);
			loader.load(Timeframes.PERIOD_W1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	static void loadGeneral(ApplicationContext context) {
	    GeneralLoader loader = context.getBean(GeneralLoader.class);
	    try {
	    	loader.load(Timeframes.PERIOD_H1);
	    	loader.load(Timeframes.PERIOD_H4);
			loader.load(Timeframes.PERIOD_D1);
			loader.load(Timeframes.PERIOD_W1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
