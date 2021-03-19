package daryl.system.databaseloader.loaders;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EurUsdLoader {

	private static final String rutaFicheroHistorico60 = "C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\h\\EURUSD_60.csv";
	private static final String rutaFicheroHistorico240 = "C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\h\\EURUSD_240.csv";
	private static final String rutaFicheroHistorico1440 = "C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\h\\EURUSD_1440.csv";
	private static final String rutaFicheroHistorico10080 = "C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\h\\EURUSD_10080.csv";
	
	public void load() {
	}
	
}
