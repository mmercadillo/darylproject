package daryl.system.databaseloader.loaders;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.databaseloader.repository.ICombinacionesArimaCRepository;
import daryl.system.databaseloader.util.Combinaciones;
import daryl.system.databaseloader.util.Ordenacion;
import daryl.system.model.CombinacionArimaC;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CombinacionesLoader {
	
	@Autowired
	Logger logger;
	
	
	@Autowired
	ICombinacionesArimaCRepository repository;

	
	public void load(List<String> valores) throws IOException, ParseException {
		
		
		for (int i = 0; i < valores.size(); i++) {
			System.out.println((i+1) + " - " + valores.get(i));
    	
			CombinacionArimaC cac = new CombinacionArimaC();
			cac.setCombinacion(valores.get(i));
			
			repository.save(cac);
		}

		
	}

}
