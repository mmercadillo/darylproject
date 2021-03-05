package daryl.system.comun.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@PropertySource("classpath:modo.properties")
@ConfigurationProperties(prefix = "modo")
@Component
class ConfigModo {

	@Getter @Setter private String test;
	@Getter @Setter private String activo;
	@Getter @Setter private String real;
	
}
