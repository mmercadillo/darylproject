package daryl.system.databaseloader.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.databaseloader.repository.IHistoricoRepository;
import daryl.system.model.historicos.Historico;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GeneralLoader extends Loader {
	
	@Autowired
	Logger logger;
	@Autowired
	protected IHistoricoRepository repository;

	private static final String HISTORICO_BASE = "/Volumes/Macintosh HD/workspaces/HISTORICOS/";
	private static final String rutaFicheroHistorico60 = HISTORICO_BASE + "XXXXXX_60.csv";
	private static final String rutaFicheroHistorico240 = HISTORICO_BASE + "XXXXXX_240.csv";
	private static final String rutaFicheroHistorico1440 = HISTORICO_BASE + "XXXXXX_1440.csv";
	private static final String rutaFicheroHistorico10080 = HISTORICO_BASE + "XXXXXX_10080.csv";
	
	public void load(Timeframes timeframe) throws IOException, ParseException {
		
		for(Activo activo : Activo.values()){
		
			File ficheroHistorico = null;
			if(timeframe == Timeframes.PERIOD_H1) {
				ficheroHistorico = new File(rutaFicheroHistorico60.replace("XXXXXX", activo.name()));
			}else if(timeframe == Timeframes.PERIOD_H4) {
				ficheroHistorico = new File(rutaFicheroHistorico240.replace("XXXXXX", activo.name()));
			}else if(timeframe == Timeframes.PERIOD_D1) {
				ficheroHistorico = new File(rutaFicheroHistorico1440.replace("XXXXXX", activo.name()));
			}else if(timeframe == Timeframes.PERIOD_W1) {
				ficheroHistorico = new File(rutaFicheroHistorico10080.replace("XXXXXX", activo.name()));
			}
			//20120101,22:00:00,1.04332,1.04342,1.04198,1.04239,140.739998936653
			if(ficheroHistorico != null) {
				
				SimpleDateFormat sdfOrigen = new SimpleDateFormat("yyyyMMddHH:mm:ss");
				SimpleDateFormat sdfDestinoFecha = new SimpleDateFormat("yyyy.MM.dd");
				SimpleDateFormat sdfDestinoHora = new SimpleDateFormat("HH:mm:ss");
				
				FileReader fr = new FileReader(ficheroHistorico);
				BufferedReader reader = new BufferedReader(fr);
				
				String leido;
				Boolean encabezado = Boolean.TRUE;
				while( (leido = reader.readLine()) != null ) {
					
					if(encabezado == Boolean.TRUE) {
						encabezado = Boolean.FALSE;
						continue;
					}
					
					String[] partes = leido.split(",");
					Date fecha = sdfOrigen.parse(partes[0]+partes[1]);
					
					Historico h = new Historico();
						h.setFecha(sdfDestinoFecha.format(fecha));
						h.setHora(sdfDestinoHora.format(fecha));
						h.setFechaHora(fecha.getTime());
						h.setApertura(new Double(partes[2]));
						h.setMaximo(new Double(partes[3]));
						h.setMinimo(new Double(partes[4]));
						h.setCierre(new Double(partes[5]));
						h.setVolumen(new Double(partes[6]));
						h.setTimeframe(timeframe);
						h.setActivo(activo);
						
					repository.save(h);
					System.out.println("Guardado -> " + h.toString());
					
				}
				reader.close();
				
			}
		}
	}
	
}
