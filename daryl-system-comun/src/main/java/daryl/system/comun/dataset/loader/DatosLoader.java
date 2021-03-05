package daryl.system.comun.dataset.loader;

import java.io.File;
import java.util.List;

import daryl.system.comun.dataset.Datos;




public abstract class DatosLoader {

	public abstract List<Datos> loadDatos(File fichero);	
	public abstract List<String> loadDatosRawFormat(File fichero);
}
 