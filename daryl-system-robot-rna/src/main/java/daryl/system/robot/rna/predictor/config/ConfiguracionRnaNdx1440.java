package daryl.system.robot.rna.predictor.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "rna-ndx-d1")
@Component
public class ConfiguracionRnaNdx1440 {

	@Getter @Setter private int neuronasEntrada;
	@Getter @Setter private int neuronasSalida;
	@Getter @Setter private int hiddenNeurons;
	@Getter @Setter private int hiddenLayers;
	@Getter @Setter private File fHistoricoLearn;
	@Getter @Setter private File fHistoricoTest;
	@Getter @Setter private File fHistoricoForecast;
	@Getter @Setter private String mode;
	@Getter @Setter private String rutaRNA;
	@Getter @Setter private Double momentum;
	@Getter @Setter private Double learningrate;
	@Getter @Setter private Double maxError;
	@Getter @Setter private int maxIterations;
	@Getter @Setter private int periodosMedia;
	
}
