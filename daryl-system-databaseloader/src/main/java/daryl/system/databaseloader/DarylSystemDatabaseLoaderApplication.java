package daryl.system.databaseloader;

import java.io.IOException;
import java.text.ParseException;

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
import daryl.system.databaseloader.loaders.EurUsdLoader;
import daryl.system.databaseloader.loaders.GdaxiLoader;
import daryl.system.databaseloader.loaders.GeneralLoader;
import daryl.system.databaseloader.loaders.NdxLoader;
import daryl.system.databaseloader.loaders.WtiLoader;
import daryl.system.databaseloader.loaders.XauUsdLoader;

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
	    
	    loadAudCad(context);
	    loadEurUsd(context);
	    loadGdaxi(context);
	    loadNdx(context);
	    loadWti(context);
	    loadXauUsd(context);
	    loadGeneral(context);
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
