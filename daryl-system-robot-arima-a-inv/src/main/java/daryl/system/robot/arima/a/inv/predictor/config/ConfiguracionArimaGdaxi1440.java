package daryl.system.robot.arima.a.inv.predictor.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "arima-gdaxi-d1")
@Component
public class ConfiguracionArimaGdaxi1440 {

	@Getter @Setter private File fHistoricoLearn;
	@Getter @Setter private String mode;
	
	//C
	@Getter @Setter private String coefficentsAr;
	@Getter @Setter private String coefficentsMa;
	@Getter @Setter private Integer std;
	@Getter @Setter private Integer inicio;
}
