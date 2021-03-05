package daryl.system.robot.arima.c.inv.inv.predictor.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "arima-gdaxi-w1")
@Component
public class ConfiguracionArimaGdaxi10080 {

	@Getter @Setter private File fHistoricoLearn;
	@Getter @Setter private String mode;
	
	//C
	@Getter @Setter private String coefficentsAr;
	@Getter @Setter private String coefficentsMa;
	@Getter @Setter private Integer std;
	@Getter @Setter private Integer inicio;
}
