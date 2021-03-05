package daryl.system.comun.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@PropertySource("classpath:ficheros.properties")
@ConfigurationProperties(prefix = "ficheros")
@Component
class ConfigFicheros {

	@Getter @Setter private String metatrader;
	@Getter @Setter private String control;
	@Getter @Setter private String historicoRnaCalc;
	@Getter @Setter private String historicoRnaPrev;
	@Getter @Setter private String operacion;
	@Getter @Setter private String forecast;
	@Getter @Setter private String extension;


	
}
