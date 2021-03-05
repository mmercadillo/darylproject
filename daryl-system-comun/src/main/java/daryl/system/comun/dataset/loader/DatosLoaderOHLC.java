package daryl.system.comun.dataset.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import daryl.system.comun.dataset.Datos;



public class DatosLoaderOHLC extends DatosLoader{
	
	private static DatosLoaderOHLC loader;
	private DatosLoaderOHLC() {
	}
	
	
	public static DatosLoaderOHLC getInstance() {
		
		if(DatosLoaderOHLC.loader == null) {
			DatosLoaderOHLC.loader = new DatosLoaderOHLC();
		}
		return DatosLoaderOHLC.loader;
		
	}

	public synchronized List<Datos> loadDatos(File fichero) {
		
		List<Datos> datos = new ArrayList<Datos>();
		try {
			
			FileReader reader = new FileReader(fichero);
			BufferedReader lector = new BufferedReader(reader);
			
			String leido;
			Boolean encabezado = Boolean.TRUE;
			while( (leido = lector.readLine()) !=  null ) {
				if(encabezado == Boolean.TRUE) {
					encabezado = Boolean.FALSE;
					continue;
				}
				
				int i = 0;
				String[] tokens = leido.split(",");
				Datos dato = 
						Datos.builder() .fecha(tokens[i++])
										.hora(tokens[i++])
										.apertura(new Double(tokens[i++]))
										.maximo(new Double(tokens[i++]))
										.minimo(new Double(tokens[i++]))
										.cierre(new Double(tokens[i++]))
										.volumen(new Double(tokens[i++]))
										.build();

				datos.add(dato);
				
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datos;
		
	}



	@Override
	public List<String> loadDatosRawFormat(File fichero) {
		
		List<String> datos = new ArrayList<String>();
		try {
			
			FileReader reader = new FileReader(fichero);
			BufferedReader lector = new BufferedReader(reader);
			
			String leido;
			while( (leido = lector.readLine()) !=  null ) {
				datos.add(leido);				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datos;
	}
	
}
