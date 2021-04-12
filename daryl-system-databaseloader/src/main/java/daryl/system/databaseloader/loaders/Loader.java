package daryl.system.databaseloader.loaders;

import java.io.IOException;
import java.text.ParseException;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.Historico;

public abstract class Loader {

	public abstract void load(Timeframes timeframe) throws IOException, ParseException;
	
}
